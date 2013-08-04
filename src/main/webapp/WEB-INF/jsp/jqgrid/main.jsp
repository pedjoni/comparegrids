<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/jqgrid/orders" var="ordersUrl"/>
<c:url value="/jqgrid/products" var="productsUrl"/>

<!doctype html>

<html lang="en">
<head>
  <link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/jquery-ui/pepper-grinder/jquery-ui-1.10.3.custom.css"/>'/>

  <script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery-1.6.4.min.js"/>'></script>
  <script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery-ui-1.10.3.custom.min.js"/>'></script>

  <title>Order Management - JQGrid</title>
  <script>
  $(function() {
    $( "#tabs" ).tabs({
      beforeLoad: function( event, ui ) {
        ui.jqXHR.error(function() {
          ui.panel.html(
            "Couldn't load this tab!" );
        });
      }
    });
  });
  </script>
</head>
<body>
 
<div id="tabs">
  <ul>
    <li><a href="#tab1">Orders</a></li>
    <li><a href="#tab2">Products</a></li>
  </ul>
  <iframe id="tab1" src="${ordersUrl}" style="width:90%;height:500px"></iframe>
  <iframe id="tab2" src="${productsUrl}" style="width:90%;height:500px"></iframe>
</div>
 
 
</body>
</html>
