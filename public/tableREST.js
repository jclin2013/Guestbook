const prepareData = (data) => {
  let metadata = [];
  metadata.push({ name: "id", label: "id", datatype: "string", editable: false});
  metadata.push({ name: "name", label: "Name", datatype: "string", editable: true});
	metadata.push({ name: "email", label:"Email", datatype: "string", editable: true});
	metadata.push({ name: "phoneNumber", label: "Phone Number", datatype: "string", editable: true});
	metadata.push({ name: "wantToEmail", label: "Want to email?", datatype: "boolean", editable: true});
	metadata.push({ name: "action", label: "Action", datatype: "html", values: "NULL", editable: false, field: "id"});

  let dataFormatted = [];
  data.forEach(dataObj => {
    dataFormatted.push({
      id: dataObj.id,
      values: {
        id: dataObj.id,
        name: dataObj.name,
        email: dataObj.email,
        phoneNumber: dataObj.phoneNumber,
        wantToEmail: dataObj.wantToEmail,
        action: dataObj.id
      }
    });
  });

  return {"metadata": metadata, "data": dataFormatted};
}


/**
 *  highlightRow and highlight are used to show a visual feedback. If the row has been successfully modified, it will be highlighted in green. Otherwise, in red
 */
function highlightRow(rowId, bgColor, after)
{
	var rowSelector = $("#" + rowId);
	rowSelector.css("background-color", bgColor);
	rowSelector.fadeTo("normal", 0.5, function() {
		rowSelector.fadeTo("fast", 1, function() {
			rowSelector.css("background-color", '');
		});
	});
}

function highlight(div_id, style) {
	highlightRow(div_id, style == "error" ? "#e5afaf" : style == "warning" ? "#ffcc00" : "#8dc70a");
}

/**
   updateCellValue calls the PHP script that will update the database.
 */
function updateCellValue(editableGrid, rowIndex, columnIndex, oldValue, newValue, row, onResponse)
{
	$.ajax({
		url: '/showAllUsers/update',
		type: 'POST',
 		data: {
			tablename : editableGrid.name,
			id: editableGrid.getRowId(rowIndex),
			newvalue: editableGrid.getColumnType(columnIndex) == "boolean" ? (newValue ? 1 : 0) : newValue,
			colname: editableGrid.getColumnName(columnIndex),
			coltype: editableGrid.getColumnType(columnIndex)
		},
		success: function (response)
		{
			// reset old value if failed then highlight row
			// var success = onResponse ? onResponse(response) : (response == "ok" || !isNaN(parseInt(response))); // by default, a sucessfull reponse can be "ok" or a database id
			// if (!success) editableGrid.setValueAt(rowIndex, columnIndex, oldValue);
		    // highlight(row.id, success ? "ok" : "error");
		    highlight(row.id, "ok");
		},
		error: function(XMLHttpRequest, textStatus, exception) { alert("Ajax failure\n" + exception); },
		async: true
	});

}

function DatabaseGrid()
{
	this.editableGrid = new EditableGrid("guestbook", {
		enableSort: true,
	    // define the number of row visible by page
      	pageSize: 50,
      // Once the table is displayed, we update the paginator state
        tableRendered:  function() {  updatePaginator(this); },
   	   //  tableLoaded: function() { datagrid.initializeGrid(this); },
				modelChanged: function(rowIndex, columnIndex, oldValue, newValue, row) {
   	    	updateCellValue(this, rowIndex, columnIndex, oldValue, newValue, row);
       	}
 	});
	this.fetchGrid();

}

DatabaseGrid.prototype.fetchGrid = function()  {
	$.ajax({
			url: "/getAllUsers",
			method: 'GET'
	}).then((data) => {
		let processedData = prepareData(data);
		this.editableGrid.load(processedData);
		this.editableGrid.renderGrid("tablecontent", "testgrid");
    datagrid.initializeGrid(this.editableGrid);
	});
};

DatabaseGrid.prototype.initializeGrid = function(grid) {

  var self = this;

// render for the action column
	grid.setCellRenderer("action", new CellRenderer({
		render: function(cell, id) {
	      cell.innerHTML+= "<i onclick=\"datagrid.deleteRow("+id+");\" class='fa fa-trash-o red' ></i>";
		}
	}));


	grid.renderGrid("tablecontent", "testgrid");
};

DatabaseGrid.prototype.deleteRow = function(id)
{
  var self = this;

  if ( confirm('Are you sure you want to delete the row id ' + id )  ) {

  $.ajax({
		url: `/showAllUsers/delete?id=${id}`,
		type: 'DELETE',
		success: function (response) { self.editableGrid.removeRow(id); },
		error: function(XMLHttpRequest, textStatus, exception) { alert("Deletion failed"); }
	});

  }

};


DatabaseGrid.prototype.addRow = function(id)
{

  var self = this;

  $.ajax({
		url: '/showAllUsers/add',
		type: 'POST',
		data: {
			name:  $("#name").val(),
			email:  $("#email").val(),
			phoneNumber:  $("#phoneNumber").val()
		},
		success: function (response)
		{
      showAddForm();
  		$("#name").val('');
      $("#email").val('');
      $("#phoneNumber").val('');

      alert("User successfully added to last row.");
      self.fetchGrid();
		},
		error: function(XMLHttpRequest, textStatus, exception) { alert("Failed to add entry"); },
	});



};

function updatePaginator(grid, divId)
{
    divId = divId || "paginator";
	var paginator = $("#" + divId).empty();
	var nbPages = grid.getPageCount();

	// get interval
	var interval = grid.getSlidingPageInterval(20);
	if (interval == null) return;

	// get pages in interval (with links except for the current page)
	var pages = grid.getPagesInInterval(interval, function(pageIndex, isCurrent) {
		if (isCurrent) return "<span id='currentpageindex'>" + (pageIndex + 1)  +"</span>";
		return $("<a>").css("cursor", "pointer").html(pageIndex + 1).click(function(event) { grid.setPageIndex(parseInt($(this).html()) - 1); });
	});

	// "first" link
	var link = $("<a class='nobg'>").html("<i class='fa fa-fast-backward'></i>");
	if (!grid.canGoBack()) link.css({ opacity : 0.4, filter: "alpha(opacity=40)" });
	else link.css("cursor", "pointer").click(function(event) { grid.firstPage(); });
	paginator.append(link);

	// "prev" link
	link = $("<a class='nobg'>").html("<i class='fa fa-backward'></i>");
	if (!grid.canGoBack()) link.css({ opacity : 0.4, filter: "alpha(opacity=40)" });
	else link.css("cursor", "pointer").click(function(event) { grid.prevPage(); });
	paginator.append(link);

	// pages
	for (p = 0; p < pages.length; p++) paginator.append(pages[p]).append(" ");

	// "next" link
	link = $("<a class='nobg'>").html("<i class='fa fa-forward'>");
	if (!grid.canGoForward()) link.css({ opacity : 0.4, filter: "alpha(opacity=40)" });
	else link.css("cursor", "pointer").click(function(event) { grid.nextPage(); });
	paginator.append(link);

	// "last" link
	link = $("<a class='nobg'>").html("<i class='fa fa-fast-forward'>");
	if (!grid.canGoForward()) link.css({ opacity : 0.4, filter: "alpha(opacity=40)" });
	else link.css("cursor", "pointer").click(function(event) { grid.lastPage(); });
	paginator.append(link);
};


function showAddForm() {
  if ( $("#addform").is(':visible') )
      $("#addform").hide();
  else
      $("#addform").show();
}
