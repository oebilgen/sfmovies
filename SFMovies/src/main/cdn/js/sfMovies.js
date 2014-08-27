var defaultCenter = new google.maps.LatLng(37.7954783, -122.4052922);
var defaultZoom = 15;
var searchCenter = new google.maps.LatLng(37.7194362, -122.4150745);
var searchZoom = 11;
var endpointPrefix = '/v1/movies/';
var map = null;
var movies = null;
var overlappingMarkerSpiderfier = null;
var infoWindow = null;
var markerCluster = null;
var usualIcon = 'images/usualIcon.png';
var spiderfiedIcon = 'images/spiderfiedIcon.png';

function initialize()
{
	map = new google.maps.Map(document.getElementById('map'),
	{
	    zoom : defaultZoom,
	    center : defaultCenter,
	    mapTypeId : google.maps.MapTypeId.ROADMAP
	});
	setTimeout(showAllMarkers, 0);
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
		        if ($(this).val().length === 0)
		        {
			        showMarkers('all', '');
		        }
	        });
        });

function showMarkers(type, query)
{
	var url = endpointPrefix + type;
	if (query != '')
	{
		url += '/' + query;
		$('#query').val(decodeURIComponent(query));
	}
	
	$.ajax(
	{
	    type : 'GET',
	    dataType : 'json',
	    url : url,
	    success : function(result)
	    {
		    if (result.success)
		    {
			    if (type != 'all')
			    {
				    if (markerCluster != null)
				    {
					    markerCluster.clearMarkers();
				    }
				    map.setZoom(searchZoom);
				    map.setCenter(searchCenter);
			    }
			    movies = result.data;
			    if (movies.length === 0)
			    {
				    alert('No results.');
				    return;
			    }
			    else
			    {
				    console.log(movies.length + ' results');
				    infoWindow = new google.maps.InfoWindow({});
				    var overlappingMarkerSpiderfier = new OverlappingMarkerSpiderfier(map,
				    {
				        markersWontMove : true,
				        markersWontHide : true,
				        legWeight : 1
				    });
				    overlappingMarkerSpiderfier.addListener('click', function(marker)
				    {
					    infoWindow.setContent(createDescription(marker.movieId, marker.locationId));
					    infoWindow.open(map, marker);
				    });
				    overlappingMarkerSpiderfier.addListener('spiderfy', function(markers)
				    {
					    for (var i = 0; i < markers.length; i++)
					    {
						    markers[i].setIcon(spiderfiedIcon);
					    }
					    infoWindow.close();
				    });
				    overlappingMarkerSpiderfier.addListener('unspiderfy', function(markers)
				    {
					    for (var i = 0; i < markers.length; i++)
					    {
						    markers[i].setIcon(usualIcon);
					    }
				    });
				    var bounds = new google.maps.LatLngBounds();
				    var markers = [];
				    var i;
				    for (i = 0; i < movies.length; i++)
				    {
					    var movie = movies[i];
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
							    console.error("DATA ERROR: title:" + movie.title + " movieLocation:%o", movieLocation);
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
						    console.log('Added marker at ' + coordinates);
					    }
				    }
				    
				    markerCluster = new MarkerClusterer(map, markers);
				    markerCluster.setMaxZoom(14);
				    
				    if (i == 0 && type == 'movieId')
				    {
					    $('#query').val('');
				    }
			    }
		    }
		    else
		    {
			    console.error('Failed to retrieve the results: ' + JSON.stringify(result.errorMessage));
		    }
	    },
	    error : function(jqXHR, textStatus, errorThrown)
	    {
		    alert('Error occured: ' + JSON.stringify(textStatus));
		    console.log('textStatus: ' + textStatus);
		    console.log('errorThrown: ' + errorThrown);
		    console.log('jqXHR' + jqXHR);
	    }
	
	});
}

function createDescription(movieId, locationId)
{
	var movie = movies[movieId];
	var description = '<div class="infoWindow"><span class="movieTitle">' + movie.title
	        + ' (<a href="#" class="infoWindowLink" onclick="showMarkers(\'releaseYear\', \'' + encodeURIComponent(movie.releaseYear) + '\')">'
	        + movie.releaseYear + '</a>)</span><br>';
	description += 'Director: <a href="#" class="infoWindowLink" onclick="showMarkers(\'director\', \'' + encodeURIComponent(movie.director) + '\')">'
	        + movie.director + '</a><br>';
	var actors = [];
	var actorsHashtag = '';
	for (var i = 0; i < movie.actors.length; i++)
	{
		actors.push('<a href="#" class="infoWindowLink" onclick="showMarkers(\'actor\', \'' + encodeURIComponent(movie.actors[i]) + '\')">' + movie.actors[i]
		        + '</a>');
		actorsHashtag += ' #' + movie.actors[i].replace(' ', '');
	}
	description += 'Actors: ' + actors.join(', ') + '<br>';
	description += 'Production Company: <a href="#" class="infoWindowLink" onclick="showMarkers(\'productionCompany\', \''
	        + encodeURIComponent(movie.productionCompany) + '\')">' + movie.productionCompany + '</a><br>';
	description += 'Distributor: <a href="#" class="infoWindowLink" onclick="showMarkers(\'distributor\', \'' + encodeURIComponent(movie.distributor) + '\')">'
	        + movie.distributor + '</a><br>';
	description += 'Writer: <a href="#" class="infoWindowLink" onclick="showMarkers(\'writer\', \'' + encodeURIComponent(movie.writer) + '\')">' + movie.writer
	        + '</a><br>';
	var formattedAddress = movie.movieLocations[locationId].formattedAddress;
	description += 'Address: <a href="https://www.google.com/maps/?q=' + encodeURIComponent(formattedAddress) + '" class="infoWindowLink" target="_blank">'
	        + formattedAddress + '</a><br>';
	description += '<div class="shareButtons"><input type="button" onclick="window.open(\'http://google.com/search?q='
	        + encodeURIComponent(movie.title + ' ' + movie.releaseYear + ' movie watch online') + '\');" value="Watch online"> ';
	description += '<input type="button" onclick="window.open(\'http://www.amazon.com/s/ref=nb_sb_noss_1?field-keywords='
	        + encodeURIComponent(movie.title + ' ' + movie.releaseYear) + ' movie\');" value="Buy on Amazon"> ';
	description += '<input type="button" onclick="window.open(\'https://www.uber.com/invite/uberoebilgen\');" value="Request Uber"> ';
	description += '<input type="button" onclick="window.open(\'/?movieId=' + movie.id + '\');" value="Link"> ';
	description += '<input type="button" onclick="window.open(\'http://twitter.com/share?url=http://www.sfmovies.org/?movieId=' + movie.id + '&text='
	        + movie.title + encodeURIComponent(' #sanfrancisco #movie' + actorsHashtag)
	        + '&via=oebilgen&related=uber\', \'\', \'width=575, height=230\');" value="Tweet"> ';
	description += '</div></div>';
	return description;
}

function getQueryString(name)
{
	name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
	return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}
var isFullScreen = false;
function toggleFullScreen()
{
	if (isFullScreen)
	{
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
		$('#fullScreenButton').prop('value', 'Theather view');
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
	}
	isFullScreen = !isFullScreen;
}
google.maps.event.addDomListener(window, "load", initialize);
