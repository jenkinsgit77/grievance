var globalFlag = false;

var customJsFunction = function (event) {

var _keyCode = evt.getKeyCode();
//     check for Enter Key
     if (_keyCode == AdfKeyStroke.ENTER_KEY ){
        if (globalFlag) {
            var exceptiondata = $(".txtPlaces input").attr("title");
            AdfCustomEvent.queue(event.getSource(), "mycustomserverEvent", 
            {
                param1 : exceptiondata
            },
            true);
            globalFlag = false;
            return true;
        }
    }

}

function googlePlaces(event) {
 var options = {
       types: ['geocode'],  // or '(cities)' if that's what you want?
       componentRestrictions: {country: "us"}
   };
    var places = new google.maps.places.Autocomplete(document.getElementsByClassName("txtPlaces")[0].children[0],options);

    var geocoder = new google.maps.Geocoder;
    google.maps.event.addListener(places, 'place_changed', function () {
        var place = places.getPlace();

        var latitude = place.geometry.location.lat();
        var longitude = place.geometry.location.lng();
        $(".latValue input").attr("value", latitude);
        $(".lngValue input").attr("value", longitude);
        var latlng = {
            lat : parseFloat(latitude), lng : parseFloat(longitude)
        };
        geocoder.geocode( {
            'location' : latlng
        },
        function (results, status) {
            if (status == 'OK') {
           //debugger;
                var addressComponents = results[0].address_components;
              //  alert()
                for (var i = 0;i < addressComponents.length;i++) {
                    if (addressComponents[i].types[0] == "postal_code") {
                        $(".txtPlaces input").attr("title", addressComponents[i].long_name);

//                        $(".latValue input").attr("value", latitude);
//                        $(".lngValue input").attr("value", longitude);
                        // $(".latValue").val("7888");
                        // $('.lngValue').val(longitude);
                        // $(".latValue").html(latitude);
                        //  $(".lngValue").html(longitude);
                        globalFlag = true;
                        return;
                    }
                }
            }
            else {
                $(".txtPlaces input").attr("title", "NA");
            }
        });
    });

}
google.maps.event.addDomListener(window, 'load', function () {
    googlePlaces();
});

function mapInit() {
    var placelatitude = 28.6139;
    var placelongitude = 77.2090;
    var placelatitudea = $(".latValue input").val();
    var placelongitudea = $(".lngValue input").val();
   
    console.log(placelatitudea);
    // alert(placelatitudea +"--"+ placelongitudea);
    if (placelatitudea != "" && placelongitudea != "") {
        var aa = parseFloat(placelatitudea);
        var bb = parseFloat(placelongitudea);
        
        placelatitude = aa;
        placelongitude = bb;

    }
    console.log(typeof(placelatitude)+"aaaa");
    console.log(typeof(placelongitude)+"aaaa");
    map = new google.maps.Map(document.getElementById('map'), 
    {
        zoom : 12, center : new google.maps.LatLng(placelatitude, placelongitude), mapTypeId : google.maps.MapTypeId.ROADMAP, styles : [{"elementType" : "geometry", "stylers" : [{"color" : "#ebe3cd"}]},{"elementType" : "labels.text.fill", "stylers" : [{"color" : "#523735"}]},{"elementType" : "labels.text.stroke", "stylers" : [{"color" : "#f5f1e6"}]},{"featureType" : "administrative", "elementType" : "geometry.stroke", "stylers" : [{"color" : "#c9b2a6"}]},{"featureType" : "administrative.land_parcel", "elementType" : "geometry.stroke", "stylers" : [{"color" : "#dcd2be"}]},{"featureType" : "administrative.land_parcel", "elementType" : "labels.text.fill", "stylers" : [{"color" : "#ae9e90"}]},{"featureType" : "landscape.natural", "elementType" : "geometry", "stylers" : [{"color" : "#dfd2ae"}]},{"featureType" : "poi", "elementType" : "geometry", "stylers" : [{"color" : "#dfd2ae"}]},{"featureType" : "poi", "elementType" : "labels.text.fill", "stylers" : [{"color" : "#93817c"}]},{"featureType" : "poi.park", "elementType" : "geometry.fill", "stylers" : [{"color" : "#a5b076"}]},{"featureType" : "poi.park", "elementType" : "labels.text.fill", "stylers" : [{"color" : "#447530"}]},{"featureType" : "road", "elementType" : "geometry", "stylers" : [{"color" : "#f5f1e6"}]},{"featureType" : "road.arterial", "elementType" : "geometry", "stylers" : [{"color" : "#fdfcf8"}]},{"featureType" : "road.highway", "elementType" : "geometry", "stylers" : [{"color" : "#f8c967"}]},{"featureType" : "road.highway", "elementType" : "geometry.stroke", "stylers" : [{"color" : "#e9bc62"}]},{"featureType" : "road.highway.controlled_access", "elementType" : "geometry", "stylers" : [{"color" : "#e98d58"}]},{"featureType" : "road.highway.controlled_access", "elementType" : "geometry.stroke", "stylers" : [{"color" : "#db8555"}]},{"featureType" : "road.local", "elementType" : "labels.text.fill", "stylers" : [{"color" : "#806b63"}]},{"featureType" : "transit.line", "elementType" : "geometry", "stylers" : [{"color" : "#dfd2ae"}]},{"featureType" : "transit.line", "elementType" : "labels.text.fill", "stylers" : [{"color" : "#8f7d77"}]},{"featureType" : "transit.line", "elementType" : "labels.text.stroke", "stylers" : [{"color" : "#ebe3cd"}]},{"featureType" : "transit.station", "elementType" : "geometry", "stylers" : [{"color" : "#dfd2ae"}]},{"featureType" : "water", "elementType" : "geometry.fill", "stylers" : [{"color" : "#b9d3c2"}]},{"featureType" : "water", "elementType" : "labels.text.fill", "stylers" : [{"color" : "#92998d"}]}]
    });
    mark = new google.maps.Marker({position:{lat:placelatitude, lng:placelongitude}, map:map});
    return map;
}

function func(paramUrl) {
       $('.panel').hide();
         $('#panel').empty();
         $('.marker').show();
       
    var myData = $(".jsonArrayData span, .jsonArrayData").html();
    //          alert(myData);
    var myDataJson = JSON.parse(myData);
    var finalData = myDataJson.LocationDetails;
    mapInit();

    var infowindow = new google.maps.InfoWindow();
    var marker, i;
    ////
    var pinImage = new google.maps.MarkerImage(paramUrl);
    //          var pinImage = new google.maps.MarkerImage("http://10.146.65.112:8888/webcenter/images/Pointer-search.png?raw=true");

    for (i = 0;i < finalData.length;i++) {
        var lbl = i + 1;
        var st = lbl.toString();
        map.setCenter(new google.maps.LatLng(finalData[0].Latitude, finalData[0].Longitude));
        marker = new google.maps.Marker( {
            position : new google.maps.LatLng(finalData[i].Latitude, finalData[i].Longitude), map : map, icon : pinImage, label :  {
                color : '#fff', fontSize : '14px', fontWeight : '600', text : st
            }
        });
        bounds = new google.maps.LatLngBounds();
        loc = new google.maps.LatLng(marker.position.lat(), marker.position.lng());
        bounds.extend(loc);
      
        google.maps.event.addListener(marker, 'click', (function (marker, i) {
            return function () {
          
              if(finalData[i].NetworkType == "AreaOffice"){
                infowindow.setContent('<div style="white-space:pre-wrap;width:250px;"><strong>' + finalData[i].Distributorname + '</strong><br/><br/>' + finalData[i].Address + '<br/>' + finalData[i].Zipcode + '<br/>' + finalData[i].StateName +'<br/><a href="javascript:void(0)" onclick="getDirection(\'' + finalData[i].Latitude+ '\',\''+finalData[i].Longitude+'\',\''+finalData[i].Address+'\')">Get direction</a></div>');
              }else{
                infowindow.setContent('<div style="white-space:pre-wrap;width:250px;"><strong>' + finalData[i].Distributorname + '</strong><br/><br/>' + finalData[i].Address + '<br/>' + finalData[i].Zipcode + '<br/>' + finalData[i].StateName +'<br/><strong>Phone No : </strong>'+finalData[i].MainPhNum +'<br/>'+finalData[i].MainEmailAddr +'<br/><strong>Ivrs No : </strong>'+finalData[i].IvrsNum +'<br/><a href="javascript:void(0)" onclick="getDirection(\'' + finalData[i].Latitude+ '\',\''+finalData[i].Longitude+'\',\''+finalData[i].Address+'\')">Get direction</a></div>');
              }
                infowindow.open(map, marker);
            }
        })(marker, i));

    }
    $(".listmarkerClick").on("click", function () {
        var textIndex = $(this).find(".listMarker span").text();
        var finalindex = textIndex - 1;
        for (i = 0;i < finalData.length;i++) {
            var lbl = i + 1;
            var st = lbl.toString();
            map.setCenter(new google.maps.LatLng(finalData[0].Latitude, finalData[0].Longitude));
            marker = new google.maps.Marker( {
                position : new google.maps.LatLng(finalData[i].Latitude, finalData[i].Longitude), map : map, icon : pinImage, label :  {
                    color : '#fff', fontSize : '14px', fontWeight : '600', text : st
                }
            });
            bounds = new google.maps.LatLngBounds();
            loc = new google.maps.LatLng(marker.position.lat(), marker.position.lng());
            bounds.extend(loc);

            google.maps.event.addListener(marker, 'click', (function (marker, i) {
                return function () {
            
                    if(finalData[i].NetworkType == "AreaOffice"){
                infowindow.setContent('<div style="white-space:pre-wrap;width:250px;"><strong>' + finalData[i].Distributorname + '</strong><br/><br/>' + finalData[i].Address + '<br/>' + finalData[i].Zipcode + '<br/>' + finalData[i].StateName +'<br/><a href="javascript:void(0)" onclick="getDirection(\'' + finalData[i].Latitude+ '\',\''+finalData[i].Longitude+'\',\''+finalData[i].Address+'\')">Get direction</a></div>');
              }else{
                infowindow.setContent('<div style="white-space:pre-wrap;width:250px;"><strong>' + finalData[i].Distributorname + '</strong><br/><br/>' + finalData[i].Address + '<br/>' + finalData[i].Zipcode + '<br/>' + finalData[i].StateName +'<br/><strong>Phone No : </strong>'+finalData[i].MainPhNum +'<br/>'+finalData[i].MainEmailAddr +'<br/><strong>Ivrs No : </strong>'+finalData[i].IvrsNum +'<br/><a href="javascript:void(0)" onclick="getDirection(\'' + finalData[i].Latitude+ '\',\''+finalData[i].Longitude+'\',\''+finalData[i].Address+'\')">Get direction</a></div>');
              }
                    infowindow.open(map, marker);
                }
            })(marker, i));

            if (finalData[i].Sno == finalindex) {
                if(finalData[i].NetworkType == "AreaOffice"){
                
                infowindow.setContent('<div style="white-space:pre-wrap;width:250px;"><strong>' + finalData[i].Distributorname + '</strong><br/><br/>' + finalData[i].Address + '<br/>' + finalData[i].Zipcode + '<br/>' + finalData[i].StateName +'<br/><a href="javascript:void(0)" onclick="getDirection(\'' + finalData[i].Latitude+ '\',\''+finalData[i].Longitude+'\',\''+finalData[i].Address+'\')">Get direction</a></div>');
              }else{
                infowindow.setContent('<div style="white-space:pre-wrap;width:250px;"><strong>' + finalData[i].Distributorname + '</strong><br/><br/>' + finalData[i].Address + '<br/>' + finalData[i].Zipcode + '<br/>' + finalData[i].StateName +'<br/><strong>Phone No : </strong>'+finalData[i].MainPhNum +'<br/>'+finalData[i].MainEmailAddr +'<br/><strong>Ivrs No : </strong>'+finalData[i].IvrsNum +'<br/><a href="javascript:void(0)" onclick="getDirection(\'' + finalData[i].Latitude+ '\',\''+finalData[i].Longitude+'\',\''+finalData[i].Address+'\')">Get direction</a></div>');
              }
                infowindow.open(map, marker);
                return false;
            }

        }
    })
    googlePlaces();
}
var curr;
window.addEventListener('DOMContentLoaded', function () {
    $(".selectMapcategory").on("change", function () {
        $(".txtPlaces input").val("").attr("title", "NA");
    });
    mapInit();
       
    if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function (position) {
            infoWindow = new google.maps.InfoWindow( {
                map : map
            });
            var pos = {
                lat : position.coords.latitude, lng : position.coords.longitude
            };
            curr={lat : position.coords.latitude, lng : position.coords.longitude}
         
            infoWindow.setPosition(pos);
            //infoWindow.setContent("Found your location Lat : " + position.coords.latitude + " Lang :" + position.coords.longitude);
            map.panTo(pos);
        });
    }
    else {
        console.log("Browser doesn't support geolocation!");
    }
    //////
    google.maps.event.addDomListener($(".txtPlaces > input"), 'keyup', function () {
        googlePlaces();
    });
    ////////////
    $(".txtPlaces > input").focusout(function () {

        var address11 = $(this).val();
        var geocoder1 = new google.maps.Geocoder;
        geocoder1.geocode( {
            'address' : address11
        },
        function (results, status) {
      
            if (status == 'OK') {
          
                //                 alert(results[0].geometry.location.lat() +"==="+results[0].geometry.location.lng());
                var latitudeloc = results[0].geometry.location.lat();
                var longitudeloc = results[0].geometry.location.lng();
                var geocoder2 = new google.maps.Geocoder;
                var latlng = {
                    lat : parseFloat(latitudeloc), lng : parseFloat(longitudeloc)
                };
                geocoder2.geocode( {
                    'location' : latlng
                },
                function (results, status) {
                    var addressComponents = results[0].address_components;
                    for (var i = 0;i < addressComponents.length;i++) {
                        if (addressComponents[i].types[0] == "postal_code") {
                            $(".txtPlaces input").attr("title", addressComponents[i].long_name);
                            globalFlag = true;
                            return;
                        }
                    }
                });

            }
            else {
                $(".txtPlaces input").attr("title", "NA");
            }
        });

    });
});


//direction

  function getDirection(dlat,dlong,address){
 // alert(dlat,dlong)
 //destination
       $('.panel').show();
       $("#destination").val(address);
         $('.marker').hide();
         var directionsService = new google.maps.DirectionsService();
         var directionsDisplay = new google.maps.DirectionsRenderer();
       
         var map = new google.maps.Map(document.getElementById('map'), {
           zoom:7,
           mapTypeId: google.maps.MapTypeId.ROADMAP
         });
        
         directionsDisplay.setMap(map);
         directionsDisplay.setPanel(document.getElementById('panel'));
     
         var request = {
           origin: curr.lat+","+curr.lng, 
           destination: dlat+","+dlong,
           travelMode: google.maps.DirectionsTravelMode.DRIVING
         };
    
         directionsService.route(request, function(response, status) {
           if (status == google.maps.DirectionsStatus.OK) {
             directionsDisplay.setDirections(response);
           }
         });
        }
        function getClose(){
           $('.panel').hide();
        
         $('.marker').show();
          mapInit();
          $( ".listmarkerClick" ).trigger( "click" );
        }
         function getDirection_btn(){
         //query-0
         //destination
         $('.panel').show();
         $('#panel').empty();
         $('.marker').hide();
         var directionsService = new google.maps.DirectionsService();
         var directionsDisplay = new google.maps.DirectionsRenderer();
       
         var map = new google.maps.Map(document.getElementById('map'), {
           zoom:7,
           mapTypeId: google.maps.MapTypeId.ROADMAP
         });
        
         directionsDisplay.setMap(map);
         directionsDisplay.setPanel(document.getElementById('panel'));
     
         var request = {
           origin: $('#query-0').val(), 
           destination: $('#destination').val(),
           travelMode: google.maps.DirectionsTravelMode.DRIVING
         };
    
         directionsService.route(request, function(response, status) {
           if (status == google.maps.DirectionsStatus.OK) {
             directionsDisplay.setDirections(response);
           }
         });
        }
        
     window.addEventListener('DOMContentLoaded', function () {
          var options = {

  componentRestrictions: {country: 'in'}
};

var autocompletes = [];
var autocomplete = new google.maps.places.Autocomplete(document.getElementById('query-0'), options);
  autocomplete.inputId = 'query-0';

  autocomplete.addListener('place_changed', fillIn);
  autocompletes.push(autocomplete);
  function fillIn() {
  console.log(this.inputId);
  var place = this.getPlace();
  console.log(place. address_components[0].long_name);
}
     })