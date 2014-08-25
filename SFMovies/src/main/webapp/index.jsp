<%
	String cdnHost = "http://cdn.sfmovies.org/";
%>
<!DOCTYPE html>
<html>
<head>
<title>San Francisco Movie Map</title>
<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon">
<link rel="icon" href="/favicon.ico" type="image/x-icon">
<link rel="stylesheet" href="<%=cdnHost%>css/sfmovies.css" />
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
    var searchResultEscape = new RegExp('(\\' + [ '/', '.', '*', '+', '?', '|', '(', ')', '[', ']', '{', '}', '\\' ].join('|\\') + ')', 'g');
    
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
				                description = 'productionCompany';
				                break;
			                case 'RELEASE_YEAR':
				                description = 'releaseYear';
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
			        if (markerCluster != null)
			        {
				        markerCluster.clearMarkers();
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
				        infoWindow = new google.maps.InfoWindow(
				        {
					        maxWidth : 400
				        });
				        var overlappingMarkerSpiderfier = new OverlappingMarkerSpiderfier(map,
				        {
				            markersWontMove : true,
				            markersWontHide : true,
				            legWeight : 1
				        });
				        overlappingMarkerSpiderfier.addListener('click', function(marker)
				        {
					        infoWindow.setContent(marker.desc);
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
				        for (var i = 0; i < movies.length; i++)
				        {
					        var movie = movies[i];
					        var location = movie.location;
					        if (location == null)
					        {
						        console.error('The movie "' + movie.title + '" does not have a known address, skipping...');
						        continue;
					        }
					        var latLng = new google.maps.LatLng(location.lat, location.lng);
					        bounds.extend(latLng);
					        var marker = new google.maps.Marker(
					        {
					            position : latLng,
					            title : movie.title,
					            icon : usualIcon,
					            info : i
					        });
					        marker.desc = createDescription(movie);
					        overlappingMarkerSpiderfier.addMarker(marker);
					        markers.push(marker);
					        console.log('Added marker at ' + latLng);
				        }
				        markerCluster = new MarkerClusterer(map, markers);
				        markerCluster.setMaxZoom(14);
			        }
		        }
		        else
		        {
			        console.error('Fail: ' + JSON.stringify(result.errorMessage));
		        }
	        },
	        error : function(request, status, error)
	        {
		        console.error('Generic Error\n\n' + JSON.stringify(request));
	        }
	    });
    }
    
    function createDescription(movie)
    {
	    var description = '<span class="movieTitle">' + movie.title + ' (<a href="#" class="releaseYear" onclick="showMarkers(\'releaseYear\', \'' + encodeURIComponent(movie.release_year) + '\')">'
	            + movie.release_year + '</a>)</span><br>';
	    description += 'Director: <a href="#" class="director" onclick="showMarkers(\'director\', \'' + encodeURIComponent(movie.director) + '\')">' + movie.director
	            + '</a><br>';
	    var actors = [];
	    if (typeof movie.actor_1 != 'undefined')
	    {
	    	actors.push('<a href="#" class="actor" onclick="showMarkers(\'actor\', \'' + encodeURIComponent(movie.actor_1) + '\')">' + movie.actor_1 + '</a>');
	    }
	    if (typeof movie.actor_2 != 'undefined')
	    {
	    	actors.push('<a href="#" class="actor" onclick="showMarkers(\'actor\', \'' + encodeURIComponent(movie.actor_2) + '\')">' + movie.actor_2 + '</a>');
	    }
	    if (typeof movie.actor_3 != 'undefined')
	    {
	    	actors.push('<a href="#" class="actor" onclick="showMarkers(\'actor\', \'' + encodeURIComponent(movie.actor_3) + '\')">' + movie.actor_3 + '</a>');
	    }
	    description += 'Actors: ' + actors.join(', ') + '<br>';
	    description += 'Production Company: <a href="#" class="productionCompany" onclick="showMarkers(\'productionCompany\', \'' + encodeURIComponent(movie.production_company) + '\')">'
        + movie.production_company + '</a><br>';
	    description += 'Distributor: <a href="#" class="distributor" onclick="showMarkers(\'distributor\', \'' + encodeURIComponent(movie.distributor) + '\')">'
	            + movie.distributor + '</a><br>';
	    description += 'Writer: <a href="#" class="writer" onclick="showMarkers(\'writer\', \'' + encodeURIComponent(movie.writer) + '\')">' + movie.writer + '</a><br>';
	    var fullAddress = movie.location.fullAddress;
	    description += 'Address: <a href="https://www.google.com/maps/?q=' + encodeURIComponent(fullAddress) + '" class="address" target="_blank">' + fullAddress + '</a>';
	    return description;
    }

    function showAllMarkers()
    {
	    showMarkers('all', '');
    }
    google.maps.event.addDomListener(window, "load", initialize);
    setTimeout(showAllMarkers, 1000);
</script>
</head>
<body>
	<div id="header">
		<input type="text" name="q" id="query"
			placeholder="Search in San Francisco Movies..." />
	</div>
	<div id="map"></div>
</body>
</html>

