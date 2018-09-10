/*
 * Kendo UI Complete v2014.1.318 (http://joyui.com)
 * Copyright 2014 Telerik AD. All rights reserved.
 *
 * Kendo UI Complete commercial licenses may be obtained at
 * http://www.telerik.com/purchase/license-agreement/joy-ui-complete
 * If you do not own a commercial license, this file shall be governed by the trial license terms.
 */
(function(window, undefined) {
	var joy = window.joy || (window.joy = {});
	joy.lang = {
		name : "en-US",
		numberFormat : {
			pattern : [ "-n" ],
			decimals : 2,
			"," : ",",
			"." : ".",
			groupSize : [ 3 ],
			percent : {
				pattern : [ "-n %", "n %" ],
				decimals : 2,
				"," : ",",
				"." : ".",
				groupSize : [ 3 ],
				symbol : "%"
			},
			currency : {
				pattern : [ "($n)", "$n" ],
				decimals : 2,
				"," : ",",
				"." : ".",
				groupSize : [ 3 ],
				symbol : "$"
			}
		},
		calendars : {
			standard : {
				days : {
					names : [ "Sunday", "Monday", "Tuesday", "Wednesday",
							"Thursday", "Friday", "Saturday" ],
					namesAbbr : [ "Sun", "Mon", "Tue", "Wed", "Thu", "Fri",
							"Sat" ],
					namesShort : [ "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" ]
				},
				months : {
					names : [ "January", "February", "March", "April", "May",
							"June", "July", "August", "September", "October",
							"November", "December", "" ],
					namesAbbr : [ "Jan", "Feb", "Mar", "Apr", "May", "Jun",
							"Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "" ]
				},
				AM : [ "AM", "am", "AM" ],
				PM : [ "PM", "pm", "PM" ],
				patterns : {
					d : "M/d/yyyy",
					D : "dddd, MMMM dd, yyyy",
					F : "dddd, MMMM dd, yyyy h:mm:ss tt",
					g : "M/d/yyyy h:mm tt",
					G : "M/d/yyyy h:mm:ss tt",
					m : "MMMM dd",
					M : "MMMM dd",
					s : "yyyy'-'MM'-'dd'T'HH':'mm':'ss",
					t : "h:mm tt",
					T : "h:mm:ss tt",
					u : "yyyy'-'MM'-'dd HH':'mm':'ss'Z'",
					y : "MMMM, yyyy",
					Y : "MMMM, yyyy"
				},
				"/" : "/",
				":" : ":",
				firstDay : 0
			}
		},
		button : {
			msg : "button message"
		},
		grid : {
			delete_confirm : "Are you sure you want to delete this record?",
			add_new_record : "Add new record",
			cancel_changes : "Cancel changes",
			save_changes : "Save changes",
			delete_record : "Delete",
			edit : "Edit",
			update : "Update",
			cancel : "Cancel"
		},
		groupable : {
			empty : "Drag a column header and drop it here to group by that column"
		},
		pager : {
			display : "{0} - {1} of {2} items",
			empty : "No items to display",
			page : "Page",
			of : "of {0}",
			itemsPerPage : "items per page",
			first : "Go to the first page",
			previous : "Go to the previous page",
			next : "Go to the next page",
			last : "Go to the last page",
			refresh : "Refresh",
			morePages : "More pages"
		},
		filtermenu : {
			EQ : "Is equal to",
			NEQ : "Is not equal to",
			string : {
				startswith : "Starts with",
				contains : "Contains",
				doesnotcontain : "Does not contain",
				endswith : "Ends with"
			},
			number : {
				gte : "Is greater than or equal to",
				gt : "Is greater than",
				lte : "Is less than or equal to",
				lt : "Is less than"
			},
			date : {
				gte : "Is after or equal to",
				gt : "Is after",
				lte : "Is before or equal to",
				lt : "Is before"
			},
			info : "Show items with value that:",
			isTrue : "is true",
			isFalse : "is false",
			filter : "Filter",
			clear : "Clear",
			and : "And",
			or : "Or",
			selectValue : "-Select value-",
			operator : "Operator",
			value : "Value",
			cancel : "Cancel",
			left : "slide",
			right : "slide:right"
		},
		columnmenu : {
			sortAscending : "Sort Ascending",
			sortDescending : "Sort Descending",
			filter : "Filter",
			columns : "Columns",
			done : "Done",
			settings : "Column Settings",
			lock : "Lock",
			unlock : "Unlock"
		},
		editor : {
			bold : "Bold",
			italic : "Italic",
			underline : "Underline",
			strikethrough : "Strikethrough",
			superscript : "Superscript",
			subscript : "Subscript",
			justifyCenter : "Center text",
			justifyLeft : "Align text left",
			justifyRight : "Align text right",
			justifyFull : "Justify",
			insertUnorderedList : "Insert unordered list",
			insertOrderedList : "Insert ordered list",
			indent : "Indent",
			outdent : "Outdent",
			createLink : "Insert hyperlink",
			unlink : "Remove hyperlink",
			insertImage : "Insert image",
			insertHtml : "Insert HTML",
			viewHtml : "View HTML",
			fontName : "Select font family",
			fontNameInherit : "(inherited font)",
			fontSize : "Select font size",
			fontSizeInherit : "(inherited size)",
			formatBlock : "Format",
			formatting : "Format",
			foreColor : "Color",
			backColor : "Background color",
			style : "Styles",
			emptyFolder : "Empty Folder",
			uploadFile : "Upload",
			orderBy : "Arrange by:",
			orderBySize : "Size",
			orderByName : "Name",
			invalidFileType : "The selected file \"{0}\" is not valid. Supported file types are {1}.",
			deleteFile : 'Are you sure you want to delete "{0}"?',
			overwriteFile : 'A file with name "{0}" already exists in the current directory. Do you want to overwrite it?',
			directoryNotFound : "A directory with this name was not found.",
			imageWebAddress : "Web address",
			imageAltText : "Alternate text",
			imageWidth : "Width (px)",
			imageHeight : "Height (px)",
			linkWebAddress : "Web address",
			linkText : "Text",
			linkToolTip : "ToolTip",
			linkOpenInNewWindow : "Open link in new window",
			dialogUpdate : "Update",
			dialogInsert : "Insert",
			dialogButtonSeparator : "or",
			dialogCancel : "Cancel",
			createTable : "Create table",
			addColumnLeft : "Add column on the left",
			addColumnRight : "Add column on the right",
			addRowAbove : "Add row above",
			addRowBelow : "Add row below",
			deleteRow : "Delete row",
			deleteColumn : "Delete column",
		    createTableA:"Create a {0} x {1} table",
		    cancelCreateTable:"Cancel"
		},
		numerictextbox:{
			upArrowText: "Increase value",
            downArrowText: "Decrease value"
		},
		slider:{
			increaseButtonTitle: "Increase",
            decreaseButtonTitle: "Decrease",
            dragHandleTitle: "drag"
		},
		scheduler:{
			   messages: {
	                today: "Today",
	                save: "Save",
	                cancel: "Cancel",
	                destroy: "Delete",
	                deleteWindowTitle: "Delete event",
	                ariaSlotLabel: "Selected from {0:t} to {1:t}",
	                ariaEventLabel: "{0} on {1:D} at {2:t}",
	                views: {
	                    day: "Day",
	                    week: "Week",
	                    workWeek: "Work Week",
	                    agenda: "Agenda",
	                    month: "Month"
	                },
	                recurrenceMessages: {
	                    deleteWindowTitle: "Delete Recurring Item",
	                    deleteWindowOccurrence: "Delete current occurrence",
	                    deleteWindowSeries: "Delete the series",
	                    editWindowTitle: "Edit Recurring Item",
	                    editWindowOccurrence: "Edit current occurrence",
	                    editWindowSeries: "Edit the series"
	                },
	                editor: {
	                    title: "Title",
	                    start: "Start",
	                    end: "End",
	                    allDayEvent: "All day event",
	                    description: "Description",
	                    repeat: "Repeat",
	                    timezone: " ",
	                    startTimezone: "Start timezone",
	                    endTimezone: "End timezone",
	                    separateTimezones: "Use separate start and end time zones",
	                    timezoneEditorTitle: "Timezones",
	                    timezoneEditorButton: "Time zone",
	                    timezoneTitle: "Time zones",
	                    noTimezone: "No timezone",
	                    editorTitle: "Event"
	                }
	            }
		}

	}
})(this);