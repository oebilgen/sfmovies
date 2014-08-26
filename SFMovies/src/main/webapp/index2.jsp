
<%
	String website = "http://www.sfmovies.org/";
	String cdnHost = "http://cdn.sfmovies.org/";
	String metaTitle = "San Francisco Movies Map";
	String metaDescription = "A website about the movies that were filmed in San Francisco...";
	String myFacebookPage = "http://www.facebook.com/oebilgen";
%>
<!DOCTYPE html>
<html>
<head>
<title><%=metaTitle%></title>
<meta property="og:title" content="<%=metaTitle%>" />
<meta name="description" content="<%=metaDescription%>">
<meta property="og:description" content="<%=metaDescription%>" />
<meta property="og:image" content="<%=cdnHost%>/img/ogImage.jpg" />
<meta property="og:url" content="<%=website%>" />
<meta property="article:publisher" content="<%=myFacebookPage%>" />
<meta property="article:author" content="<%=myFacebookPage%>" />
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<link rel="icon" href="/favicon.ico" type="image/x-icon">
<link rel="stylesheet" href="<%=cdnHost%>css/sfmovies.css" />
<link rel="stylesheet" href="<%=cdnHost%>css/sfmoviesMobile.css" />
<style>

#header {
	width: 100%;
	height: 50px;
	margin: 0px auto 0px auto;
}

#query {
	height: 49px;
	font-size: 24px;
	text-indent: 12px;
}
</style>
<script src="<%=cdnHost%>js/jquery.min.js"></script>
<script src="<%=cdnHost%>js/googlemaps.js"></script>
<script type="text/javascript" src="<%=cdnHost%>js/markerclusterer.js"></script>
<script type="text/javascript" src="<%=cdnHost%>js/oms.min.js"></script>
<script type="text/javascript"
	src="<%=cdnHost%>js/jquery.autocomplete.min.js"></script>
<script>
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
    var usualIcon = '<%=cdnHost%>images/usualIcon.png';
    var spiderfiedIcon = '<%=cdnHost%>images/spiderfiedIcon.png';
    
    function initialize()
    {
	    map = new google.maps.Map(document.getElementById('map'),
	    {
	        zoom : defaultZoom,
	        center : defaultCenter,
	        mapTypeId : google.maps.MapTypeId.ROADMAP
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
		                
		                return value.value.replace(data, '<strong>' + data + '</strong>') + ' <span class="searchDescription"><font color="silver">'
		                        + description + '</font></span>';
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
				        alert('No results');
				        showAllMarkers();
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
						        var latLng = new google.maps.LatLng(movieLocation.latitude, movieLocation.longitude);
						        bounds.extend(latLng);
						        var marker = new google.maps.Marker(
						        {
						            position : latLng,
						            title : movie.title,
						            icon : usualIcon,
						            movieId : i,
						            locationId : j
						        });
						        overlappingMarkerSpiderfier.addMarker(marker);
						        markers.push(marker);
						        console.log('Added marker at ' + latLng);
					        }
				        }
				        if (i == 0 && type == 'movieId')
				        {
					        $('#query').val('');
				        }
				        markerCluster = new MarkerClusterer(map, markers);
				        markerCluster.setMaxZoom(14);
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
	    description += 'Distributor: <a href="#" class="infoWindowLink" onclick="showMarkers(\'distributor\', \'' + encodeURIComponent(movie.distributor)
	            + '\')">' + movie.distributor + '</a><br>';
	    description += 'Writer: <a href="#" class="infoWindowLink" onclick="showMarkers(\'writer\', \'' + encodeURIComponent(movie.writer) + '\')">' + movie.writer
	            + '</a><br>';
	    var formattedAddress = movie.movieLocations[locationId].formattedAddress;
	    description += 'Address: <a href="https://www.google.com/maps/?q=' + encodeURIComponent(formattedAddress) + '" class="infoWindowLink" target="_blank">'
	            + formattedAddress + '</a><br>';
	    description += '<div class="shareButtons"><input type="button" onclick="window.open(\'http://google.com/search?q=' + encodeURIComponent(movie.title + ' watch online')
	            + '\');" value="Watch online"> ';
	    description += '<input type="button" onclick="window.open(\'https://www.uber.com/invite/uberoebilgen\');" value="Request Uber"> ';
	    description += '<input type="button" onclick="window.open(\'/?movieId=' + movie.id + '\');" value="Link"> ';
	    description += '<input type="button" onclick="window.open(\'http://twitter.com/share?url=<%=website%>?movieId=' + movie.id + '&text=' + movie.title + encodeURIComponent(' #sanfrancisco #movie' + actorsHashtag) + '&via=oebilgen&related=uber\', \'\', \'width=575, height=230\');" value="Tweet"> ';
	    description += '</div></div>';
	    return description;
    }

    function showAllMarkers()
    {
		<%
			String movieId = request.getParameter("movieId");
			if (movieId == null || movieId.isEmpty()) {
				out.print("showMarkers('all', '');");
			} else {
				out.print("showMarkers('movieId', '" + movieId + "');");
			}
		%>
	}
    google.maps.event.addDomListener(window, "load", initialize);
    setTimeout(showAllMarkers, 1000);
</script>
</head>
<body>
	<div id="header">
		<input type="text" name="q" id="query"
			placeholder="Search for San Francisco Movies..." autofocus />
	</div>
	<div id="map"></div>
</body>
</html>

