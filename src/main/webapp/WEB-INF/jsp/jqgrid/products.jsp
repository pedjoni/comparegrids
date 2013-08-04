<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/jqgrid/products/records" var="recordsUrl"/>
<c:url value="/jqgrid/products/create" var="addUrl"/>
<c:url value="/jqgrid/products/update" var="editUrl"/>
<c:url value="/jqgrid/products/delete" var="deleteUrl"/>

<html>
<head>
	<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/jquery-ui/pepper-grinder/jquery-ui-1.10.3.custom.css"/>'/>
	<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/ui.jqgrid-4.4.3.css"/>'/>
	<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/style.css"/>'/>
	
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery-1.6.4.min.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery-ui-1.10.3.custom.min.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/grid.locale-en-4.4.3.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery.jqGrid.min.4.4.3.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/custom.js"/>'></script>
	
	<title>Product Records</title>
	
	<script type='text/javascript'>
	$(function() {
		$("#grid2").jqGrid({
		   	url:'${recordsUrl}',
			datatype: 'json',
			mtype: 'GET',
		   	colNames:['Id', 'Product Name', 'Items Available', 'Price'],
		   	colModel:[
		   		{name:'id',index:'id', width:25, editable:false, editoptions:{readonly:true, size:10}, hidden:false},
		   		{name:'name',index:'name', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
		   		{name:'itemsAvailable',index:'itemsAvailable', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
		   		{name:'price',index:'price', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}}
		   	],
		   	postData: {},
			rowNum:10,
		   	rowList:[10,20,40,60],
		   	height: 240,
		   	autowidth: true,
			rownumbers: true,
		   	pager: '#pager2',
		   	sortname: 'id',
		    viewrecords: true,
		    sortorder: "asc",
		    caption:"Records",
		    emptyrecords: "Empty records",
		    loadonce: false,
		    loadComplete: function() {},
		    jsonReader : {
		        root: "rows",
		        page: "page",
		        total: "total",
		        records: "records",
		        repeatitems: false,
		        cell: "cell",
		        id: "id"
		    }
		});

		$("#grid2").jqGrid('navGrid','#pager2',
				{edit:false, add:false, del:false, search:true},
				{}, {}, {}, 
				{ 	// search
					sopt:['cn', 'eq', 'ne', 'lt', 'gt', 'bw', 'ew'],
					closeOnEscape: true, 
					multipleSearch: true, 
					closeAfterSearch: true
				}
		);
		
		$("#grid2").navButtonAdd('#pager2',
				{ 	caption:"Add", 
					buttonicon:"ui-icon-plus", 
					onClickButton: addRow,
					position: "last", 
					title:"", 
					cursor: "pointer"
				} 
		);
		
		$("#grid2").navButtonAdd('#pager2',
				{ 	caption:"Edit", 
					buttonicon:"ui-icon-pencil", 
					onClickButton: editRow,
					position: "last", 
					title:"", 
					cursor: "pointer"
				} 
		);
		
		$("#grid2").navButtonAdd('#pager2',
			{ 	caption:"Delete", 
				buttonicon:"ui-icon-trash", 
				onClickButton: deleteRow,
				position: "last", 
				title:"", 
				cursor: "pointer"
			} 
		);

		// Toolbar Search
		$("#grid2").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true, defaultSearch:"cn"});
	});

	function addRow() {
   		$("#grid2").jqGrid('setColProp', 'name', {editoptions:{readonly:false, size:10}});
   		$("#grid2").jqGrid('setColProp', 'itemsAvailable', {editrules:{required:true}});
   		$("#grid2").jqGrid('setColProp', 'price', {editrules:{required:true}});
   		
		// Get the currently selected row
		$('#grid2').jqGrid('editGridRow','new',
	    		{ 	url: '${addUrl}', 
					editData: {},
	                serializeEditData: function(data){ 
	                    data.id = 0; 
	                    return $.param(data);
	                },
				    recreateForm: true,
				    beforeShowForm: function(form) {
			            $('#pData').hide();  
			            $('#nData').hide();
				    },
					beforeInitData: function(form) {},
					closeAfterAdd: true,
					reloadAfterSubmit:true,
					afterSubmit : function(response, postdata) 
					{ 
				        var result = eval('(' + response.responseText + ')');
						var errors = "";
						
				        if (result.success == false) {
							for (var i = 0; i < result.message.length; i++) {
								errors +=  result.message[i] + "<br/>";
							}
				        }  else {
				        	$('#msgbox2').text('Entry has been added successfully');
							$('#msgbox2').dialog( 
									{	title: 'Success',
										modal: true,
										buttons: {"Ok": function()  {
											$(this).dialog("close");} 
										}
									});
		                }
				    	// only used for adding new records
				    	var newId = null;
				    	
						return [result.success, errors, newId];
					}
	    		});

   		// $("#grid2").jqGrid('setColProp', 'password', {hidden: true});
	} // end of addRow


	function editRow() {
   		$("#grid2").jqGrid('setColProp', 'name', {editoptions:{readonly:true, size:10}});
   		
		// Get the currently selected row
		var row = $('#grid2').jqGrid('getGridParam','selrow');
		
		if( row != null ) {
		
			$('#grid2').jqGrid('editGridRow', row,
				{	url: '${editUrl}', 
					editData: {},
			        recreateForm: true,
			        beforeShowForm: function(form) {
			            $('#pData').hide();  
			            $('#nData').hide();
			        },
					beforeInitData: function(form) {},
					closeAfterEdit: true,
					reloadAfterSubmit:true,
					afterSubmit : function(response, postdata) 
					{ 
			            var result = eval('(' + response.responseText + ')');
						var errors = "";
						
			            if (result.success == false) {
							for (var i = 0; i < result.message.length; i++) {
								errors +=  result.message[i] + "<br/>";
							}
			            }  else {
			            	$('#msgbox2').text('Entry has been edited successfully');
							$('#msgbox2').dialog( 
									{	title: 'Success',
										modal: true,
										buttons: {"Ok": function()  {
											$(this).dialog("close");} 
										}
									});
		                }
				    	// only used for adding new records
				    	var newId = null;
			        	
						return [result.success, errors, newId];
					}
				});
		} else {
			$('#msgbox2').text('You must select a record first!');
			$('#msgbox2').dialog( 
					{	title: 'Error',
						modal: true,
						buttons: {"Ok": function()  {
							$(this).dialog("close");} 
						}
					});
		}
	}
	
	function deleteRow() {
		// Get the currently selected row
	    var row = $('#grid2').jqGrid('getGridParam','selrow');

	    // A pop-up dialog will appear to confirm the selected action
		if( row != null ) 
			$('#grid2').jqGrid( 'delGridRow', row,
	          	{	url:'${deleteUrl}', 
					recreateForm: true,
				    beforeShowForm: function(form) {
				    	//Change title
				        $(".delmsg").replaceWith('<span style="white-space: pre;">' +
				        		'Delete selected record?' + '</span>');
		            	//hide arrows
				        $('#pData').hide();  
				        $('#nData').hide();
				    },
	          		reloadAfterSubmit:true,
	          		closeAfterDelete: true,
	          		serializeDelData: function (postdata) {
		          	      var rowdata = $('#grid2').getRowData(postdata.id);
		          	      // append postdata with any information 
		          	      return {id: postdata.id, oper: postdata.oper, name: rowdata.name};
		          	},
	          		afterSubmit : function(response, postdata) 
					{ 
			            var result = eval('(' + response.responseText + ')');
						var errors = "";
						
			            if (result.success == false) {
							for (var i = 0; i < result.message.length; i++) {
								errors +=  result.message[i] + "<br/>";
							}
			            }  else {
			            	$('#msgbox2').text('Entry has been deleted successfully');
							$('#msgbox2').dialog( 
									{	title: 'Success',
										modal: true,
										buttons: {"Ok": function()  {
											$(this).dialog("close");} 
										}
									});
		                }
				    	// only used for adding new records
				    	var newId = null;
			        	
						return [result.success, errors, newId];
					}
	          	});
		else {
			$('#msgbox2').text('You must select a record first!');
			$('#msgbox2').dialog( 
					{	title: 'Error',
						modal: true,
						buttons: {"Ok": function()  {
							$(this).dialog("close");} 
						}
					});
		}
	}
	</script>
</head>

<body>
	<h1 id='banner2'>Product Inventory</h1>
	
	<div id='jqgrid2'>
		<table id='grid2'></table>
		<div id='pager2'></div>
	</div>
	
	<div id='msgbox2' title='' style='display:none'></div>
</body>
</html>