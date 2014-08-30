
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
<link rel="stylesheet"
	href="<%=cdnHost%>css/sfMovies.201408300317.min.css" />
<link rel="stylesheet"
	href="<%=cdnHost%>css/sfMoviesMobile.201408291854.min.css"
	media="only screen and (max-device-width:480px)" />
<script src="<%=cdnHost%>js/jquery.201408270533.min.js"></script>
<script src="//maps.googleapis.com/maps/api/js"></script>
<script type="text/javascript"
	src="<%=cdnHost%>js/markerClusterer.201408270533.min.js"></script>
<script type="text/javascript"
	src="<%=cdnHost%>js/oms.201408270533.min.js"></script>
<script type="text/javascript"
	src="<%=cdnHost%>js/jquery.autocomplete.201408270533.min.js"></script>
<script type="text/javascript"
	src="<%=cdnHost%>js/sfMovies.201408300317.min.js"></script>
<script>
	setMovieId(getQueryString('movieId'));
</script>
</head>
<body>
	<div id="header">
		<div id="fullScreenControlDiv">
			<input type="button" value="Full Screen" id="fullScreenButton"
				onclick="toggleFullScreen();" />
		</div>
		<input type="text" name="q" id="query"
			placeholder="Search for San Francisco Movies..." autofocus />
	</div>
	<div id="map"></div>
</body>
</html>
