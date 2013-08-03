<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:url value="/jqgrid/orders/records" var="recordsUrl"/>
<c:url value="/jqgrid/orders/create" var="addUrl"/>
<c:url value="/jqgrid/orders/update" var="editUrl"/>
<c:url value="/jqgrid/orders/delete" var="deleteUrl"/>

<html>
<head>
	<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/jquery-ui/pepper-grinder/jquery-ui-1.8.16.custom.css"/>'/>
	<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/ui.jqgrid-4.4.3.css"/>'/>
	<link rel="stylesheet" type="text/css" media="screen" href='<c:url value="/resources/css/jqgrid/style.css"/>'/>
	
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery-1.6.4.min.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery-ui-1.8.16.custom.min.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/grid.locale-en-4.4.3.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/jquery.jqGrid.min.4.4.3.js"/>'></script>
	<script type='text/javascript' src='<c:url value="/resources/js/jqgrid/custom.js"/>'></script>
	
	<title>Order Records</title>
	
	<script type='text/javascript'>
	var grid11intvlId;
	$(function() {
		$("#grid1").jqGrid({
		   	url:'${recordsUrl}',
			datatype: 'json',
			mtype: 'GET',
		   	colNames:['Id', 'Product Id', 'Product Name', 'Price', 'Quantity', 'Date Received', 'Status'],
		   	colModel:[
		   		{name:'id', index:'id', width:55, editable:false, editoptions:{readonly:true}, hidden:true},
		   		{name:'product.id', index:'product.id', width:55, editable:true, editrules:{required:true}, editoptions:{size:10}, hidden:true},
		   		{name:'product.name', index:'product.name', width:100, editable:false},
		   		{name:'price', index:'price', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
		   		{name:'quantity', index:'quantity', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}},
		   		{name:'recievedDate', index:'recievedDate', width:100, editable:false, 
		   			formatter: function (cellval, opts) {
		   				var date = new Date(cellval);
						opts = $.extend({}, $.jgrid.formatter.date, opts);
						return $.fmatter.util.DateFormat("", date, 'd-M-Y', opts);
					}
				},
		   		{name:'status', index:'status', width:100, editable:true, editrules:{required:true}, editoptions:{size:10}, edittype:'select', formatter:'select', editoptions:{value:{1:'New',2:'Processed',3:'Canceled',4:'Errored'}}}
		   	],
		   	postData: {},
			rowNum:10,
		   	rowList:[10,20,40,60],
		   	height: 240,
		   	autowidth: true,
			rownumbers: true,
		   	pager: '#pager1',
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

		$("#grid1").jqGrid('navGrid','#pager1',
				{edit:false, add:false, del:false, search:true},
				{}, {}, {}, 
				{ 	// search
					sopt:['cn', 'eq', 'ne', 'lt', 'gt', 'bw', 'ew'],
					closeOnEscape: true, 
					multipleSearch: true, 
					closeAfterSearch: true
				}
		);
		
		$("#grid1").navButtonAdd('#pager1',
				{ 	caption:"Add", 
					buttonicon:"ui-icon-plus", 
					onClickButton: addRow,
					position: "last", 
					title:"", 
					cursor: "pointer"
				} 
		);
		
		$("#grid1").navButtonAdd('#pager1',
				{ 	caption:"Edit", 
					buttonicon:"ui-icon-pencil", 
					onClickButton: editRow,
					position: "last", 
					title:"", 
					cursor: "pointer"
				} 
		);
		
		$("#grid1").navButtonAdd('#pager1',
			{ 	caption:"Delete", 
				buttonicon:"ui-icon-trash", 
				onClickButton: deleteRow,
				position: "last", 
				title:"", 
				cursor: "pointer"
			} 
		);

		// Toolbar Search
		$("#grid1").jqGrid('filterToolbar',{stringResult: true,searchOnEnter : true, defaultSearch:"cn"});
		
		clearInterval(grid11intvlId); //clear previous interval if it exist (when clicked, jquery tabs will reload content but won't clear any initiated interval )
		grid11intvlId = setInterval(
			function() {
				$("#grid1").trigger("reloadGrid",[{current:true}]); // current:true preserves the selection
			},
			30*1000
		); // intervalId can be used to stop the grid reload later: clearInterval(intervalId);
	});

	function addRow() {
   		$(this).jqGrid('setColProp', 'product.id', {hidden:false});
   		$(this).jqGrid('setColProp', 'status', {editable:false, hidden:true});
		// Get the currently selected row
		$('#grid1').jqGrid('editGridRow','new',
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
				        	$('#msgbox1').text('Entry has been added successfully');
							$('#msgbox1').dialog( 
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
	    $(this).jqGrid('setColProp', 'product.id', {hidden:true});
	    $(this).jqGrid('setColProp', 'status', {editable:true, hidden:false});

	} // end of addRow


	function editRow() {
	
		// Get the currently selected row
		var row = $('#grid1').jqGrid('getGridParam','selrow');
		
		if( row != null ) {
		
			$('#grid1').jqGrid('editGridRow', row,
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
			            	$('#msgbox1').text('Entry has been edited successfully');
							$('#msgbox1').dialog( 
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
			$('#msgbox1').text('You must select a record first!');
			$('#msgbox1').dialog( 
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
	    var row = $('#grid1').jqGrid('getGridParam','selrow');

	    // A pop-up dialog will appear to confirm the selected action
		if( row != null ) 
			$('#grid1').jqGrid( 'delGridRow', row,
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
		          	      var rowdata = $('#grid1').getRowData(postdata.id);
		          	      // append postdata with any information 
		          	      return {id: postdata.id, oper: postdata.oper};
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
			            	$('#msgbox1').text('Entry has been deleted successfully');
							$('#msgbox1').dialog( 
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
			$('#msgbox1').text('You must select a record first!');
			$('#msgbox1').dialog( 
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
	<h1 id='banner'>Order Inventory</h1>
	
	<div id='jqgrid'>
		<table id='grid1'></table>
		<div id='pager1'></div>
	</div>
	
	<div id='msgbox1' title='' style='display:none'></div>
</body>
</html>