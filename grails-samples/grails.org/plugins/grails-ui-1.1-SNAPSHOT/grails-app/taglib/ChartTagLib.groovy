import org.codehaus.groovy.grails.plugins.grailsui.GrailsUIException

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

class ChartTagLib {

    static namespace = "gui"

    static def SERIES_KEYS = ['displayName','yField','style']

    def grailsUITagLibService
    def chartTagLibService

    def lineChart = { attrs ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        yAxis: [type:'numeric']
                ],
                attrs,
                ['data', 'seriesDefs', 'renderTo']
        )
        def id = attrs.remove('id')
        def renderTo = attrs.remove('renderTo') ?: id
        def jsid = grailsUITagLibService.toJS(id)
        def data = attrs.remove('data')
        def seriesDefs = attrs.remove('seriesDefs')

        // making 2 assumptions here:
        //  1. All rows have the same fields
        //  2. All columns should be included in the chart
        def fields = data[0].collect { it.key }

        // give a reference to the seriesDef we are creating in the config object
        attrs.series = '@seriesDef'

        // if there is no xField set, assume the first field is x
        attrs.xField = attrs.xField ?: fields[0]

        def axisCode = chartTagLibService.generateAxisCode(attrs.yAxis, "GRAILSUI.${jsid}_yAxis")
        attrs.yAxis = "@GRAILSUI.${jsid}_yAxis"

        def newSeriesDefs = grailsUITagLibService.processShortcutSyntax(seriesDefs, 'yField', 'displayName', SERIES_KEYS, [name:'LineChart',tagName:'lineChart'])
        def seriesDefCode = "var seriesDef = [${grailsUITagLibService.listToConfig(newSeriesDefs)}];"

        out << """
        <script>
            GRAILSUI.${jsid}_data = [${grailsUITagLibService.listToConfig(data)}];
            GRAILSUI.${jsid}_ds = new YAHOO.util.DataSource( GRAILSUI.${jsid}_data );
            GRAILSUI.${jsid}_ds.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
            GRAILSUI.${jsid}_ds.responseSchema = {fields: [${grailsUITagLibService.listToConfig(fields)}]};
            ${seriesDefCode}
            ${axisCode}
            GRAILSUI.${jsid} = new YAHOO.widget.LineChart("$renderTo", GRAILSUI.${jsid}_ds, {${grailsUITagLibService.mapToConfig(attrs)}});
        </script>
        """
    }

    def barChart = { attrs ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        xAxis: [type:'numeric']
                ],
                attrs,
                ['data', 'seriesDefs', 'renderTo']
        )
        def id = attrs.remove('id')
        def renderTo = attrs.remove('renderTo') ?: id
        def jsid = grailsUITagLibService.toJS(id)
        def data = attrs.remove('data')
        def seriesDefs = attrs.remove('seriesDefs')

        // making 2 assumptions here:
        //  1. All rows have the same fields
        //  2. All columns should be included in the chart
        def fields = data[0].collect { it.key }

        // if there is no xField set, assume the first field is y
        attrs.yField = attrs.yField ?: fields[0]

        def axisCode = chartTagLibService.generateAxisCode(attrs.xAxis, "GRAILSUI.${jsid}_xAxis")
        attrs.xAxis = "@GRAILSUI.${jsid}_xAxis"

        // give a reference to the seriesDef we are creating in the config object
        attrs.series = '@seriesDef'
        
        def newSeriesDefs = grailsUITagLibService.processShortcutSyntax(seriesDefs, 'xField', 'displayName', SERIES_KEYS, [name:'LineChart',tagName:'lineChart'])
        def seriesDefCode = "var seriesDef = [${grailsUITagLibService.listToConfig(newSeriesDefs)}];"

        out << """
        <script>
            GRAILSUI.${jsid}_data = [${grailsUITagLibService.listToConfig(data)}];
            GRAILSUI.${jsid}_ds = new YAHOO.util.DataSource( GRAILSUI.${jsid}_data );
            GRAILSUI.${jsid}_ds.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
            GRAILSUI.${jsid}_ds.responseSchema = {fields: [${grailsUITagLibService.listToConfig(fields)}]};
            ${seriesDefCode}
            ${axisCode}
            GRAILSUI.${jsid} = new YAHOO.widget.BarChart("$renderTo", GRAILSUI.${jsid}_ds, {${grailsUITagLibService.mapToConfig(attrs)}});
        </script>
        """
    }

    def columnChart = { attrs ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                ],
                attrs,
                ['data', 'seriesDefs', 'renderTo']
        )
        def id = attrs.remove('id')
        def renderTo = attrs.remove('renderTo') ?: id
        def jsid = grailsUITagLibService.toJS(id)
        def data = attrs.remove('data')
        def seriesDefs = attrs.remove('seriesDefs')

        // making 2 assumptions here:
        //  1. All rows have the same fields
        //  2. All columns should be included in the chart
        def fields = data[0].collect { it.key }

        // if there is no xField set, assume the first field is x
        attrs.xField = attrs.xField ?: fields[0]

        // give a reference to the seriesDef we are creating in the config object
        attrs.series = '@seriesDef'

        def newSeriesDefs = grailsUITagLibService.processShortcutSyntax(seriesDefs, 'yField', 'displayName', SERIES_KEYS, [name:'ColumnChart',tagName:'gui:columnChart'])
        def seriesDefCode = "var seriesDef = [${grailsUITagLibService.listToConfig(newSeriesDefs)}];"

        out << """
        <script>
            GRAILSUI.${jsid}_data = [${grailsUITagLibService.listToConfig(data)}];
            GRAILSUI.${jsid}_ds = new YAHOO.util.DataSource( GRAILSUI.${jsid}_data );
            GRAILSUI.${jsid}_ds.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
            GRAILSUI.${jsid}_ds.responseSchema = {fields: [${grailsUITagLibService.listToConfig(fields)}]};
            ${seriesDefCode}
            GRAILSUI.${jsid} = new YAHOO.widget.ColumnChart("$renderTo", GRAILSUI.${jsid}_ds, {${grailsUITagLibService.mapToConfig(attrs)}});
        </script>
        """
    }

    def pieChart = { attrs ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                ],
                attrs,
                ['data', 'renderTo']
        )
        def id = attrs.remove('id')
        def renderTo = attrs.remove('renderTo') ?: id
        def jsid = grailsUITagLibService.toJS(id)
        def data = attrs.remove('data')

        // making 2 assumptions here:
        //  1. All rows have the same fields
        //  2. All columns should be included in the chart
        def fields = data[0].collect { it.key }

        out << """
        <script>
            GRAILSUI.${jsid}_data = [${grailsUITagLibService.listToConfig(data)}];
            GRAILSUI.${jsid}_ds = new YAHOO.util.DataSource( GRAILSUI.${jsid}_data );
            GRAILSUI.${jsid}_ds.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
            GRAILSUI.${jsid}_ds.responseSchema = {fields: [${grailsUITagLibService.listToConfig(fields)}]};
            GRAILSUI.${jsid} = new YAHOO.widget.PieChart("$renderTo", GRAILSUI.${jsid}_ds, {${grailsUITagLibService.mapToConfig(attrs)}});
        </script>
        """
    }
}