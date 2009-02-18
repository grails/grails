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
 */
import org.codehaus.groovy.grails.plugins.grailsui.GrailsUIException

class DataTableTagLib {

    static namespace = "gui"

    static def COL_KEYS = ['key','field','label','abbr','children','className','editor','editorOptions','formatter','hidden',
            'maxAutoWidth','minWidth','resizeable','selected','sortable','sortOptions','width']

    def dataTableTagLibService
    def grailsUITagLibService

    /**
     * DataTable creates a YUI DataTable component markup with JavaScript to transform it.
     *
     * @id id
     * @resultsList expected JSON root of the data to populate the table
     * @sortedBy initial column to sort by.  This should be in defined in the columnDefs mapping, unless allowExclusiveSort=true,
     * in which case the server will have to handle the initial sorting.
     * @sortOrder asc or desc
     * @rowExpansion true/false, will attempt to find a url to load additional markup in an expansion element on row click
     * @allowExclusiveSort default is false, when true will allow unrecognized column names to be used as initial sort value
     */
    def dataTable = {attrs ->

        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        selectionMode: 'single',
                        resultsList: 'results',
                        recordOffset: 0,
                        rowsPerPage: 10,
                        sortedBy: 'id',
                        sortOrder: 'asc',
                        rowExpansion: false,
                        rowClickNavigate: false,
                        rowClickMode: 'none',
                        params: [:],
                        formatter: 'text',
                        connMethodPost:true,
                        allowExclusiveSort:false,
                        suppressCellEditorLoadingDialog:false,
                        paginatorConfig:[:]
                ],
                attrs,
                ['columnDefs', 'controller', 'action']
        )

        def columnDefs = grailsUITagLibService.processShortcutSyntax(attrs.remove('columnDefs'), 'key', 'label', COL_KEYS, [name:'DataTable',tagName:'gui:dataTable'])

        def sortedBy = attrs.remove('sortedBy')
        def allowExclusiveSort = attrs.remove('allowExclusiveSort') ?: false
        // if sortedBy refers to a field that doesn't exist in the column defs, throw an exception to warn that the table can't render
        def colDefsContainsSortedBy = columnDefs.find {it.key == sortedBy}
        if (!colDefsContainsSortedBy && !allowExclusiveSort) {
            throw new GrailsUIException("The GrailsUI dataTable cannot be defined with a 'sortedBy' value of '${sortedBy}' because it "
                + "does not exist in the column definition.  To fix, either update the columnDefs attribute to contain the sortedBy "
                + "value, change sortedBy to refer to a column defined in 'columnDefs', or add \"allowExclusiveSort='true'\" to the tag "
                + "attributes (assuming the server will sort on a field not defined within the datatable column definitions).")
        }

        // can't have rowExpansion and rowClickNavigation both true
        def rowExpansion = attrs.remove('rowExpansion')
        def rowClickNavigation = attrs.remove('rowClickNavigation')
        if (rowExpansion && rowClickNavigation) {
            throw new GrailsUIException('\'rowExpansion\' and \'rowClickNavigation\' cannot both be '
                    + 'true.  Only one row click handler is allowed.  To fix, remove one, or set one to false.')
        }

        // the GRAILSUI.DataTable.rowClickMode is either 'none', 'expand', or 'navigate'
        if (rowExpansion) attrs.rowClickMode = 'expand'
        if (rowClickNavigation) attrs.rowClickMode = 'navigate'

        def initialSortedByConfigString = !colDefsContainsSortedBy && allowExclusiveSort ? '' : "sortedBy               : {key: \"${sortedBy}\", dir: YAHOO.widget.DataTable.CLASS_ASC},"
        def id = attrs.remove('id')
        def jsid = grailsUITagLibService.toJS(id)
        def params = attrs.remove('params')
        def connMethodPost = attrs.remove('connMethodPost')
        def sortOrder = attrs.remove('sortOrder')
        def recordOffset = attrs.remove('recordOffset')
        def rowsPerPage = attrs.remove('rowsPerPage')
        def paginatorConfig = attrs.remove('paginatorConfig')
        def suppressCellEditorLoadingDialog = attrs.remove('suppressCellEditorLoadingDialog')
        if (paginatorConfig.rowsPerPage) rowsPerPage = paginatorConfig.rowsPerPage
        def paramQueryString = params.collect {key,val -> "$key=$val" }.join('&')

        def dataUrl = createLink(attrs)

        // build query for initial request
        def query = "max=${rowsPerPage}&offset=${recordOffset}&sort=${sortedBy}&order=${sortOrder}&${paramQueryString}"

        // set up any editors within columnDefs
        def editorConstruction = configureCellEditors(columnDefs)

        // if row expansion is enabled, add the dataUrl to the datasource
        if (rowExpansion || rowClickNavigation) {
            columnDefs << [key: 'dataUrl', type: 'dataDrillDown', hidden: true]
        }

        if (rowsPerPage) { paginatorConfig.rowsPerPage = rowsPerPage }

        out << """
            <div id="dt_div_${id}"></div>
            <script>
            YAHOO.util.Event.onDOMReady(function () {
                var DataSource = YAHOO.util.DataSource,
                    DataTable  = YAHOO.widget.DataTable,
                    Paginator  = YAHOO.widget.Paginator;

                var ${jsid}_ds = new DataSource('${dataUrl}?');
                ${jsid}_ds.responseType   = DataSource.TYPE_JSON;
                ${jsid}_ds.connMethodPost=${connMethodPost};
                ${jsid}_ds.responseSchema = {
                    resultsList : '${attrs.remove('resultsList')}',
                    fields      : [${columnDefs.collect { "\"$it.key\""}.join(',')}],
                    metaFields  : {
                        totalRecords: 'totalRecords'
                    }
                };
                ${jsid}_ds.doBeforeCallback = function(oRequest, oFullResponse, oParsedResponse, oCallback) {
                    return GRAILSUI.util.replaceDateStringsWithRealDates(oParsedResponse);
                };

                var ${jsid}_paginator = new Paginator(
                    {${grailsUITagLibService.mapToConfig paginatorConfig}}
                );

                var registerEditorListener = function(editor, field, url) {
                    editor.subscribe("saveEvent", function(oArgs) {
                        ${if (!suppressCellEditorLoadingDialog) {return "GRAILSUI.${jsid}.loadingDialog.show();"}}
                        var editorCallback = {
                            failure: function(o) {
                                // revert the cell value
                                GRAILSUI.${jsid}.updateCell(oArgs.editor.getRecord(), field, oArgs.oldData);
                                // alert user
                                alert('Recieved an error during edit: ' + o.responseText);
                            }
                        };
                        YAHOO.util.Connect.asyncRequest('POST', url, editorCallback, 'id=' + oArgs.editor.getRecord().getData('id') + '&field=' + field + '&newValue=' + oArgs.newData);
                    });
                };

                ${editorConstruction}

                var myColumnDefs = [${grailsUITagLibService.listToConfig columnDefs}];

                GRAILSUI.${jsid} = new GRAILSUI.DataTable('dt_div_${id}', myColumnDefs, ${jsid}_ds, '${paramQueryString}', {
                    initialRequest         : '${query}',
                    paginator              : ${jsid}_paginator,
                    dynamicData            : true,
                    ${initialSortedByConfigString}
                    ${grailsUITagLibService.mapToConfig attrs}
                });
                // Update totalRecords on the fly with value from server
                GRAILSUI.${jsid}.handleDataReturnPayload = function(oRequest, oResponse, oPayload) {
                    oPayload.totalRecords = oResponse.meta.totalRecords;
                    return oPayload;
                };

                // Set up editing flow
                var highlightEditableCell = function(oArgs) {
                    var elCell = oArgs.target;
                    if(YAHOO.util.Dom.hasClass(elCell, "yui-dt-editable")) {
                        this.highlightCell(elCell);
                    }
                };
                GRAILSUI.${jsid}.subscribe("cellMouseoverEvent", highlightEditableCell);
                GRAILSUI.${jsid}.subscribe("cellMouseoutEvent", GRAILSUI.${jsid}.onEventUnhighlightCell);
                GRAILSUI.${jsid}.subscribe("cellClickEvent", GRAILSUI.${jsid}.onEventShowCellEditor);
            });
            </script>
        """
    }

    private def configureCellEditors(colDefs) {
        def editorConstruction = ''
        colDefs.each { configMap ->
            configMap.each { key, val ->
                if (key == 'editor') {
                    def editorName = "${configMap.key}_cellEditor"
                    def editorType = val.type ?: configMap.formatter
                    switch (editorType.toLowerCase()) {
                        case 'text':
                            editorConstruction += "var ${editorName} = new YAHOO.widget.TextboxCellEditor({${grailsUITagLibService.mapToConfig(val.config)}});\n"
                            break;
                        case 'dropdown':
                            if (!val.config.dropdownOptions) {
                                throw new GrailsUIException("Cannot specify editor type of '${editorType}' without providing dropdownOptions in editor config.")
                            }
                            editorConstruction += "var ${editorName} = new YAHOO.widget.DropdownCellEditor({${grailsUITagLibService.mapToConfig(val.config)}});\n"
                            break;
                        case 'date':
                            editorConstruction += "var ${editorName} = new YAHOO.widget.DateCellEditor();\n"
                            break;
                        case 'checkbox':
                            editorConstruction += "var ${editorName} = new YAHOO.widget.CheckboxCellEditor({${grailsUITagLibService.mapToConfig(val.config)}});\n"
                            break;
                        case 'radio':
                            editorConstruction += "var ${editorName} = new YAHOO.widget.RadioCellEditor({${grailsUITagLibService.mapToConfig(val.config)}});\n"
                            break;
                        default:
                            throw new GrailsUIException("Cannot handle cell editor of type '${editorType}'")
                    }
                    configMap[key] = "@${editorName}"
                    def url = createLink(val)
                    editorConstruction += "registerEditorListener(${editorName}, '${configMap.key}', '${url}');\n"
                }
            }
        }
        editorConstruction
    }

}