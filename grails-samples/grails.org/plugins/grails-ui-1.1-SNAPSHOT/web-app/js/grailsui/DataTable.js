/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Data Table YUI Extension
 * ------------------------
 * | Provides row YUI DataTable functionality, plus row expansion
 * ------------------------
 *   author: Matthew Taylor
 *   contributor: Zach Leatherman (http://www.zachleat.com/web/2007/08/28/enlarging-your-yui-datatable-in-29-seconds-or-less)
 *   contributor: Marcel Overdijk (http://marceloverdijk.blogspot.com/2008/06/grails-yui-datatable-example.html)
 *
 * elContainer <HTMLElement>  Container element for the TABLE.
 * aColumnDefs <Object[]> Array of object literal Column definitions. 
 * oDataSource <YAHOO.util.DataSource> DataSource instance. 
 * oConfigs <object> (optional) Object literal of configuration values.
 */
GRAILSUI.DataTable = function(elContainer, aColumnDefs, oDataSource, queryString, oConfig) {
    if (arguments.length > 0) {
        this.customQueryString = queryString;
        this.loadingDialog = new GRAILSUI.LoadingDialog(elContainer + 'loading', null);
        if (oConfig['collapseOnExpansionClick'] != undefined) {
            this.collapseOnExpansionClick = oConfig['collapseOnExpansionClick'];
        }
        if (oConfig['rowClickMode'] != undefined) {
            this.rowClickMode = oConfig['rowClickMode'];
        }
        GRAILSUI.DataTable.superclass.constructor.call(this, elContainer, aColumnDefs, oDataSource, oConfig);
    }
    this._initSelfListeners();
};

YAHOO.lang.extend(GRAILSUI.DataTable, YAHOO.widget.DataTable, {

    loadingDialog: null,
    collapseOnExpansionClick: false,
    rowClickMode: 'none', // none, expand, or navigate
    customQueryString: null,

    _initSelfListeners: function() {
        this.subscribe('headerRowMousedownEvent', this.cleanup);
        this.subscribe('rowClickEvent', this.rowSelectionMade, this._showNewContent);
        this.subscribe('renderEvent', function() {
            this.loadingDialog.hide();
        });
    },

    _showNewContent: function(newDiv) {
        var selectedRows = this.getSelectedRows();
        var url = this.getRecord(selectedRows[0]).getData().dataUrl;
        if( selectedRows.length > 0) {
            var callback = {
                success: function(o) {
                    GRAILSUI.util.replaceWithServerResponse(newDiv, o);
                },
                failure: function(o) {
                    alert('There was a problem loading data for that row from ' + url);
                }
            };
            YAHOO.util.Connect.asyncRequest('POST', url, callback);
        }
    },

    // translates sorting and pagination values into a query that the grails controller will accept
    buildQueryString: function(state) {
        var query = "offset=" + state.pagination.recordOffset +
               "&max=" + state.pagination.rowsPerPage +
               "&sort=" + state.sorting.key +
               "&order=" + ((state.sorting.dir === YAHOO.widget.DataTable.CLASS_ASC) ? "asc" : "desc");
        if (this.customQueryString != null) {
            query += '&' + this.customQueryString;
        }
        return query;
    },

    cleanup: function(args) {
        if( this.get('selectionMode' ) == 'single' ) {
            this.collapseAll();
            this.unselectAllRows();
        }
    },

    rowSelectionMade: function(args, callback) {
        // if an already expanded section is being clicked, we don't want to proceess a selection so the
        // user can interact with the expansion properly
        var targetIsExpansion = YAHOO.util.Dom.hasClass(args.target,'ymod-expandedData');
        var targetAncestorIsExpansion = YAHOO.util.Dom.getAncestorByClassName(args.target, 'ymod-expandedData') != undefined;
        var expandedClick = targetIsExpansion || targetAncestorIsExpansion;
        // but if user set to always collapse on expansion click, set it back to false
        if (this.collapseOnExpansionClick) {
            expandedClick = false;
        }
        if( this.get( 'selectionMode' ) == 'single' && !expandedClick) {
            this.collapseAll();
            if (YAHOO.util.Dom.hasClass(args.target, 'yui-dt-selected')) {
                this.unselectAllRows();
            } else {
                this.onEventSelectRow.call( this, args );
            }
            // ignore everything in none mode
            if (this.rowClickMode == 'none') return;
            // only click and expand if the response schema defines a data url for row expansion
            var dataUrlIndex = this.getDataSource().responseSchema.fields.join().search('dataUrl');
            if (dataUrlIndex > 0) {
                if (this.rowClickMode == 'expand') {
                    this.clickAndExpand(args[ 'event' ], callback);
                } else {
                    window.location = this.getRecord(this.getSelectedTrEls()[0]).getData().dataUrl;
                }
            }
        }
    },

    // provides paginations stuff
    onPaginatorChangeRequest: function(state) {
        this.loadingDialog.show();
        
        this.cleanup();
        var sortedBy  = this.get('sortedBy');

        // Define the new state
        var newState = {
            startIndex: state.recordOffset,
            sorting: {
                key: sortedBy.key,
                dir: ((sortedBy.dir === YAHOO.widget.DataTable.CLASS_DESC) ? YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC)
            },
            pagination : { // Pagination values
                recordOffset: state.recordOffset, // Default to first page when sorting
                rowsPerPage: this.get("paginator").getRowsPerPage()
            }
        };

        // Create callback object for the request
        var oCallback = {
            success: this.onDataReturnSetRows,
            failure: this.onDataReturnSetRows,
            scope: this,
            argument: newState // Pass in new state as data payload for callback function to use
        };

        
        // Send the request
        this.getDataSource().sendRequest(this.buildQueryString(newState), oCallback);
    },

    collapseAll: function() {
        var nodes = YAHOO.util.Dom.getElementsByClassName( 'ymod-expanded', 'tr', this );
        for( var j = 0, t = nodes.length; j < t; j++ )
        {
            nodes[ j ].parentNode.removeChild( nodes[ j ].nextSibling );
            YAHOO.util.Dom.removeClass( nodes[ j ], 'ymod-expanded' );
        }
    },

    collapseRow: function(row) {
    	YAHOO.util.Dom.removeClass( row.previousSibling, 'ymod-expanded' );
    	row.parentNode.removeChild( row );
    },

    clickAndExpand: function( e, content ) {
        var selectedRows = this.getSelectedTrEls();
        if( selectedRows.length > 0 ) {
            if( YAHOO.util.Dom.hasClass( selectedRows[ 0 ], 'ymod-expandedData' ) ) {
                YAHOO.util.Dom.removeClass( selectedRows[ 0 ].previousSibling, 'ymod-expanded' );
                selectedRows[ 0 ].parentNode.removeChild( selectedRows[ 0 ] );
            } else if( !YAHOO.util.Dom.hasClass( selectedRows[ 0 ], 'ymod-expanded' ) ) {
                var newRow = document.createElement( 'tr' );
                var newCell = document.createElement( 'td' );
                var newDiv = document.createElement( 'div' );
                YAHOO.util.Dom.addClass( newDiv, 'ymod-expandedDataContent' );
                if( content != null && YAHOO.lang.isFunction( content ) ) {
                    content.call( this, newDiv );
                } else {
                    throw new Error( 'A function is needed for the content callback in GRAILSUI.DataTable.' );
                }
                newCell.appendChild( newDiv );
                newCell.colSpan = selectedRows[ 0 ].childNodes.length;
                newRow.appendChild( newCell );
                YAHOO.util.Dom.addClass( newRow, 'ymod-expandedData' );
                YAHOO.util.Dom.addClass( newRow, 'yui-dt-selected' );
                if( YAHOO.util.Dom.hasClass( selectedRows[ 0 ], 'yui-dt-odd' ) ) YAHOO.util.Dom.addClass( newRow, 'yui-dt-odd' );
                else if( YAHOO.util.Dom.hasClass( selectedRows[ 0 ], 'yui-dt-even' ) ) YAHOO.util.Dom.addClass( newRow, 'yui-dt-even' );
                YAHOO.util.Dom.addClass( selectedRows[ 0 ], 'ymod-expanded' );

                if( selectedRows[ 0 ].nextSibling != null )
                    selectedRows[ 0 ].parentNode.insertBefore( newRow, selectedRows[ 0 ].nextSibling );
                else
                    selectedRows[ 0 ].parentNode.appendChild( newRow );

                if (collapseOnExpansionClick) {
                    YAHOO.util.Event.addListener( newRow, 'click', function( e, dataTable ) {
                        // get the target
                        var tar;
                        if (e.target) tar = e.target;
                        else if (e.srcElement) tar = e.srcElement;
    
                        if (tar.nodeType == 3) tar = tar.parentNode; // work around Safari bug
    
                        dataTable.collapseRow( this );
                        dataTable.unselectAllRows();
    
                        // see if the target is an anchor tag
                        //this way the link event will still go through
                        if(tar.tagName != "A"){
                           YAHOO.util.Event.stopEvent( e );
                        }
                    }, this );
                }
                YAHOO.util.Event.stopEvent( e );
                return newDiv;
            } else {
                selectedRows[ 0 ].parentNode.removeChild( selectedRows[ 0 ].nextSibling );
                YAHOO.util.Dom.removeClass( selectedRows[ 0 ], 'ymod-expanded' );
                YAHOO.util.Event.stopEvent( e );
            }
        }
    },

    // provides custom server-side sorting
    sortColumn: function(oColumn) {
        this.loadingDialog.show();
        
        // Default ascending
        var sDir = "asc";

        // If already sorted, sort in opposite direction
        var currentSort = this.get("sortedBy");
        if(currentSort != undefined && oColumn.key === currentSort.key) {
            sDir = (this.get("sortedBy").dir === YAHOO.widget.DataTable.CLASS_ASC) ? "desc" : "asc";
        }

        // Define the new state
        var newState = {
            startIndex: 0,
            sorting: { // Sort values
                key: oColumn.key,
                dir: (sDir === "desc") ? YAHOO.widget.DataTable.CLASS_DESC : YAHOO.widget.DataTable.CLASS_ASC
            },
            pagination : { // Pagination values
                recordOffset: 0, // Default to first page when sorting
                rowsPerPage: this.get("paginator").getRowsPerPage()
            }
        };

        // Create callback object for the request
        var oCallback = {
            success: this.onDataReturnSetRows,
            failure: this.onDataReturnSetRows,
            scope: this,
            argument: newState // Pass in new state as data payload for callback function to use
        };

        // Send the request
        this.getDataSource().sendRequest(this.buildQueryString(newState), oCallback);
    }
});
