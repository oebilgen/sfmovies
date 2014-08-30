var defaultCenter = new google.maps.LatLng(37.7954783, -122.4052922);
var defaultZoom = 15;
var endpointPrefix = '/v1/movies/';
var map = null;
var movieSummarys = null;
var overlappingMarkerSpiderfier = null;
var infoWindow = new google.maps.InfoWindow({});
var infoWindowContents = {};
var markerCluster = null;
var usualIcon = 'http://cdn.sfmovies.org/images/usualIcon.201408270533.png';
var spiderfiedIcon = 'http://cdn.sfmovies.org/images/spiderfiedIcon.201408270533.png';
var isFullScreen = false;
var boundsChangeExpected = false;
var reloadRequested = false;
var lastType = 'all';
var lastQuery = '';
var lastMapBounds = null;

function setMovieId(movieId)
{
	if (movieId == null || movieId.trim() == '')
	{
		lastType = 'all';
		lastQuery = '';
	}
	else
	{
		lastType = 'movieId';
		lastQuery = movieId;
	}
}

function initializeMap()
{
	map = new google.maps.Map(document.getElementById('map'),
	{
	    zoom : defaultZoom,
	    center : defaultCenter,
	    mapTypeId : google.maps.MapTypeId.ROADMAP
	});
	google.maps.event.addListener(map, 'bounds_changed', function()
	{
		if (boundsChangeExpected)
		{
			console.debug('Bounds change is expected, skipping event...');
			return;
		}
		console.info('Map reload is requested.');
		reloadRequested = true;
	});
	google.maps.event.addListener(map, 'idle', function()
	{
		if (lastQuery != '' && lastMapBounds != null)
		{
			console.info('Last query is not empty [' + lastQuery + '], skipping map draw...');
			return;
		}
		if (boundsChangeExpected)
		{
			console.info('Bounds change is expected, skipping map draw...');
			boundsChangeExpected = false;
			return;
		}
		if (reloadRequested)
		{
			showMarkers(lastType, lastQuery);
		}
		reloadRequested = false;
	});
	overlappingMarkerSpiderfier = new OverlappingMarkerSpiderfier(map,
	{
	    markersWontMove : true,
	    markersWontHide : true,
	    legWeight : 1
	});
	overlappingMarkerSpiderfier.addListener('click', function(marker)
	{
		boundsChangeExpected = true;
		var movieId = movieSummarys[marker.movieId].id;
		if (movieId in infoWindowContents)
		{
			infoWindow.setContent(infoWindowContents[movieId]);
			infoWindow.open(map, marker);
			return;
		}
		
		var movieUrl = endpointPrefix + 'movieDetail/' + movieId;
		$.ajax(
		{
		    type : 'GET',
		    dataType : 'json',
		    url : movieUrl,
		    success : function(remoteMovie)
		    {
			    if (remoteMovie == null)
			    {
				    alert('Movie ID=[' + movieId + '] not found.');
				    return;
			    }
			    boundsChangeExpected = true;
			    infoWindow.setContent(createInfoWindowContent(remoteMovie));
			    infoWindow.open(map, marker);
		    },
		    error : function(jqXHR, textStatus, errorThrown)
		    {
			    handleAjaxError(jqXHR, textStatus, errorThrown);
		    }
		});
	});
	overlappingMarkerSpiderfier.addListener('spiderfy', function(markers)
	{
		boundsChangeExpected = true;
		for (var i = 0; i < markers.length; i++)
		{
			markers[i].setIcon(spiderfiedIcon);
		}
		infoWindow.close();
	});
	overlappingMarkerSpiderfier.addListener('unspiderfy', function(markers)
	{
		boundsChangeExpected = true;
		for (var i = 0; i < markers.length; i++)
		{
			markers[i].setIcon(usualIcon);
		}
	});
}

$(document).ready(
        function()
        {
	        var options, a;
	        options =
	        {
	            serviceUrl : endpointPrefix + 'search',
	            showNoSuggestionNotice : true,
	            maxHeight : '60%',
	            minChars : 3,
	            onSelect : function(value, data)
	            {
		            var endpoint = '';
		            switch (value.data)
		            {
			            case 'ACTOR':
				            endpoint = 'actor';
				            break;
			            case 'DIRECTOR':
				            endpoint = 'director';
				            break;
			            case 'DISTRIBUTOR':
				            endpoint = 'distributor';
				            break;
			            case 'PRODUCTION_COMPANY':
				            endpoint = 'productionCompany';
				            break;
			            case 'RELEASE_YEAR':
				            endpoint = 'releaseYear';
				            break;
			            case 'TITLE':
				            endpoint = 'title';
				            break;
			            case 'WRITER':
				            endpoint = 'writer';
				            break;
			            default:
				            alert("Unknown search result: " + data);
				            return;
		            }
		            showMarkers(endpoint, value.value);
	            },
	            formatResult : function(value, data, currentValue)
	            {
		            var description = '';
		            switch (value.data)
		            {
			            case 'ACTOR':
				            description = 'actor';
				            break;
			            case 'DIRECTOR':
				            description = 'director';
				            break;
			            case 'DISTRIBUTOR':
				            description = 'distributor';
				            break;
			            case 'PRODUCTION_COMPANY':
				            description = 'production company';
				            break;
			            case 'RELEASE_YEAR':
				            description = 'release year';
				            break;
			            case 'TITLE':
				            description = 'title';
				            break;
			            case 'WRITER':
				            description = 'writer';
				            break;
			            default:
				            alert("Unknown search result: " + value.data);
				            return;
		            }
		            
		            return value.value.replace(data, '<strong>' + data + '</strong>') + ' <span class="searchDescription"><font color="silver">' + description
		                    + '</font></span>';
	            }
	        };
	        a = $('#query').autocomplete(options);
	        $('#query').keyup(function(e)
	        {
		        if ($(this).val().length == 0)
		        {
			        lastType = 'all';
			        lastQuery = '';
			        showMarkers(lastType, lastQuery);
		        }
	        });
        });

function getMapBounds()
{
	var mapBounds = map.getBounds();
	var mapBoundsSouthWest = mapBounds.getSouthWest();
	var mapBoundsNorthEast = mapBounds.getNorthEast();
	var parameter = 'swc=' + mapBoundsSouthWest.lat() + ',' + mapBoundsSouthWest.lng();
	parameter += '&nec=' + mapBoundsNorthEast.lat() + ',' + mapBoundsNorthEast.lng();
	return parameter;
}

function showMarkers(type, query)
{
	console.info('Call: showMarkers(' + type + ',' + query + ')');
	var newMapBounds = getMapBounds();
	if (lastType == type && lastQuery == query && lastMapBounds == newMapBounds)
	{
		console.info('Nothing changed, skipping showMarkers...');
		return;
	}
	lastType = type;
	lastQuery = query;
	if (markerCluster != null)
	{
		markerCluster.clearMarkers();
	}
	if (overlappingMarkerSpiderfier != null)
	{
		overlappingMarkerSpiderfier.clearMarkers();
	}
	var url = endpointPrefix + type;
	if (query != '')
	{
		url += '/' + query;
		$('#query').val(decodeURIComponent(query));
	}
	if (type == 'all')
	{
		url += '?' + newMapBounds;
	}
	console.info("Ajax URL: " + url);
	$.ajax(
	{
	    type : 'GET',
	    dataType : 'json',
	    url : url,
	    success : function(result)
	    {
		    if (result.success)
		    {
			    movieSummarys = result.data;
			    if (movieSummarys.length == 0)
			    {
				    if (type != 'all')
				    {
					    alert('No results.');
				    }
				    return;
			    }
			    console.log(movieSummarys.length + ' result(s).');
			    var bounds = new google.maps.LatLngBounds();
			    var markers = [];
			    var i;
			    for (i = 0; i < movieSummarys.length; i++)
			    {
				    var movie = movieSummarys[i];
				    if (type == 'movieId')
				    {
					    $('#query').val(movie.title);
				    }
				    var movieLocations = movie.movieLocations;
				    if (movieLocations == null || movieLocations.length == 0)
				    {
					    console.info('The movie "' + movie.title + '" does not have a known address, skipping...');
					    continue;
				    }
				    for (var j = 0; j < movieLocations.length; j++)
				    {
					    movieLocation = movieLocations[j];
					    if (movieLocation == null)
					    {
						    console.error('DATA ERROR: title:' + movie.title + ' movieLocation:%o', movieLocation);
						    continue;
					    }
					    var coordinates = new google.maps.LatLng(movieLocation.latitude, movieLocation.longitude);
					    bounds.extend(coordinates);
					    var marker = new google.maps.Marker(
					    {
					        position : coordinates,
					        title : movie.title,
					        icon : usualIcon,
					        movieId : i,
					        locationId : j
					    });
					    overlappingMarkerSpiderfier.addMarker(marker);
					    markers.push(marker);
					    console.log(movie.title + ' marker at ' + coordinates);
				    }
			    }
			    
			    markerCluster = new MarkerClusterer(map, markers);
			    markerCluster.setMaxZoom(14);
			    if (type != 'all')
			    {
				    boundsChangeExpected = true;
				    map.fitBounds(bounds);
				    lastMapBounds = bounds;
			    }
			    
			    if (i == 0 && type == 'movieId')
			    {
				    $('#query').val('');
			    }
			    
		    }
		    else
		    {
			    console.error('Failed to retrieve the results: ' + JSON.stringify(result.errorMessage));
		    }
	    },
	    error : function(jqXHR, textStatus, errorThrown)
	    {
		    handleAjaxError(jqXHR, textStatus, errorThrown);
	    }
	});
}

function handleAjaxError(jqXHR, textStatus, errorThrown)
{
	alert('Error occured: ' + JSON.stringify(textStatus));
	console.log('textStatus: ' + textStatus);
	console.log('errorThrown: ' + errorThrown);
	console.log('jqXHR' + jqXHR);
}

function createInfoWindowContent(movie)
{
	var movieSummary = movie.movieSummary;
	if (movieSummary.id in infoWindowContents)
	{
		return infoWindowContents[movieSummary.id];
	}
	var title = movieSummary.title;
	var movieLocations = movieSummary.movieLocations;
	var infoWindowContent = '<div class="infoWindow"><span class="movieTitle">' + title
	        + ' (<a href="#" class="infoWindowLink" onclick="showMarkers(\'releaseYear\', \'' + encodeURIComponent(movie.releaseYear) + '\')">'
	        + movie.releaseYear + '</a>)</span><br>';
	infoWindowContent += 'Director: <a href="#" class="infoWindowLink" onclick="showMarkers(\'director\', \'' + encodeURIComponent(movie.director) + '\')">'
	        + movie.director + '</a><br>';
	var actors = [];
	var actorsHashtag = '';
	for (var i = 0; i < movie.actors.length; i++)
	{
		actors.push('<a href="#" class="infoWindowLink" onclick="showMarkers(\'actor\', \'' + encodeURIComponent(movie.actors[i]) + '\')">' + movie.actors[i]
		        + '</a>');
		actorsHashtag += ' #' + movie.actors[i].replace(' ', '');
	}
	infoWindowContent += 'Actors: ' + actors.join(', ') + '<br>';
	infoWindowContent += 'Production Company: <a href="#" class="infoWindowLink" onclick="showMarkers(\'productionCompany\', \''
	        + encodeURIComponent(movie.productionCompany) + '\')">' + movie.productionCompany + '</a><br>';
	infoWindowContent += 'Distributor: <a href="#" class="infoWindowLink" onclick="showMarkers(\'distributor\', \'' + encodeURIComponent(movie.distributor)
	        + '\')">' + movie.distributor + '</a><br>';
	infoWindowContent += 'Writer: <a href="#" class="infoWindowLink" onclick="showMarkers(\'writer\', \'' + encodeURIComponent(movie.writer) + '\')">'
	        + movie.writer + '</a><br>';
	infoWindowContent += 'Addresses:<br>';
	for (var i = 0; i < movieLocations.length; i++)
	{
		var movieLocation = movieLocations[i];
		infoWindowContent += '<li><a href="https://www.google.com/maps/?q=' + encodeURIComponent(movieLocation.formattedAddress)
		        + '" class="infoWindowLink" target="_blank">' + movieLocation.formattedAddress + '</a>';
		if (movieLocation.funFacts != null)
		{
			infoWindowContent += '<br><span class="funFacts">' + movieLocation.funFacts + '</span>';
		}
		infoWindowContent += '</li>';
	}
	infoWindowContent += '<div class="shareButtons"><input type="button" onclick="window.open(\'http://google.com/search?q='
	        + encodeURIComponent(title + ' ' + movie.releaseYear + ' movie watch online') + '\');" value="Watch online"> ';
	infoWindowContent += '<input type="button" onclick="window.open(\'http://www.amazon.com/s/ref=nb_sb_noss_1?field-keywords='
	        + encodeURIComponent(title + ' ' + movie.releaseYear) + ' movie\');" value="Buy on Amazon"> ';
	infoWindowContent += '<input type="button" onclick="window.open(\'http://google.com/search?q='
	        + encodeURIComponent(title + ' ' + movie.releaseYear + ' movie imdb') + '&btnI\');" value="IMDb"> ';
	infoWindowContent += '<input type="button" onclick="window.open(\'http://google.com/search?q='
	        + encodeURIComponent(title + ' ' + movie.releaseYear + ' movie wikipedia') + '&btnI\');" value="Wikipedia"> ';
	infoWindowContent += '<input type="button" onclick="window.open(\'/?movieId=' + movieSummary.id + '\');" value="Link"> ';
	infoWindowContent += '<input type="button" onclick="window.open(\'http://twitter.com/share?url=http://www.sfmovies.org/?movieId=' + movieSummary.id
	        + '&text=' + title + encodeURIComponent(' #sanfrancisco #movie' + actorsHashtag)
	        + '&via=oebilgen&related=uber\', \'\', \'width=575, height=230\');" value="Tweet"> ';
	infoWindowContent += '</div></div>';
	infoWindowContents[movieSummary.id] = infoWindowContent;
	return infoWindowContent;
}

function getQueryString(name)
{
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
	return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}

function toggleFullScreen()
{
	if (isFullScreen)
	{
		boundsChangeExpected = true;
		$('#fullScreenButton').prop('value', 'Full screen');
		$('#fullScreenButton').animate(
		{
			margin : '58px 72px auto auto'
		});
		$('#header').animate(
		{
		    width : '90%',
		    margin : '50px auto 0px auto'
		});
		$('#map').animate(
		{
		    width : '90%',
		    height : '450px'
		});
	}
	else
	{
		boundsChangeExpected = false;
		reloadRequested = true;
		$('#fullScreenButton').prop('value', 'Theatre view');
		$('#fullScreenButton').animate(
		{
			margin : '8px 0px auto auto'
		});
		$('#header').animate(
		{
		    width : '100%',
		    margin : '0px auto 0px auto'
		});
		$('#map').animate(
		{
		    width : '100%',
		    height : '100%'
		});
		map.setCenter(map.getCenter());
	}
	setTimeout(function()
	{
		google.maps.event.trigger(map, 'resize');
	}, 500);
	
	isFullScreen = !isFullScreen;
}

function printStackTrace()
{
	var e = new Error('dummy');
	var stack = e.stack.replace(/^[^\(]+?[\n$]/gm, '').replace(/^\s+at\s+/gm, '').replace(/^Object.<anonymous>\s*\(/gm, '{anonymous}()@').split('\n');
	console.error(stack);
}

(function(i, s, o, g, r, a, m)
{
	i['GoogleAnalyticsObject'] = r;
	i[r] = i[r] || function()
	{
		(i[r].q = i[r].q || []).push(arguments)
	}, i[r].l = 1 * new Date();
	a = s.createElement(o), m = s.getElementsByTagName(o)[0];
	a.async = 1;
	a.src = g;
	m.parentNode.insertBefore(a, m)
})(window, document, 'script', '//www.google-analytics.com/analytics.js', 'ga');

ga('create', 'UA-41345435-2', 'auto');
ga('send', 'pageview');

google.maps.event.addDomListener(window, 'load', initializeMap);
