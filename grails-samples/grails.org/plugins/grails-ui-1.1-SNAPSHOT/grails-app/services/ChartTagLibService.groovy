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

class ChartTagLibService {
    static transactional = false

    def grailsUITagLibService

    def generateAxisCode(axis, name) {
        // create a config holder in case there's not one
        if (!axis.config) axis.config = [:]
        def result = ''
        // deal with the number format, if there is one...
        if (axis.format) {
            result += """
            ${name}_format = function( value ) {
                return YAHOO.util.Number.format( value, {${grailsUITagLibService.mapToConfig(axis.format)}} );
            }
            """
            axis.config.labelFunction = "@${name}_format"
        }

        def constructor
        switch (axis.type) {
            case 'numeric':
                constructor = "new YAHOO.widget.NumericAxis()"
                break;
            default:
                throw new GrailsUIException("Cannot handle chart axis type of '$axis.type'")
        }
        result += "${name} = ${constructor};\n"

        def propAssignments = axis.config.collect {
            "${name}.${it.key} = ${grailsUITagLibService.valueToConfig(it.value)};"
        }.join('\n')
        result += "${propAssignments}"

        result
    }
}