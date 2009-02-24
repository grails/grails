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
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

class ResourcesTagLib implements ApplicationContextAware {

    static namespace = "gui"

    def applicationContext
    def resourcesTagLibService
    
    // Only using the application context so we have access to GraulsUiConfig
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext
    }

    /**
     * Used to identify what resources will be used on a gsp page.  This allows only one tag to be included in the
     * header, that contains all the grailsUI components to be used.  It writes out a list of script and css
     * include tags that is optimized for the components that are included.
     *
     *  -- attrs.components = String with comma delimited component names that will be used on the gsp page
     *  -- attrs.mode = 'minimal', 'debug', or 'raw' (if null, defaults to 'minimal')
     */
    def resources = {attrs ->
        def config = applicationContext.getBean('grailsUIConfig')

        def mode = attrs.remove('mode')
        if (!mode) mode = 'minimal'
        // ensure mode is within bounds
        if (!(mode in config.mode.keySet())) {
            throw new GrailsUIException("Mode must be within ${config.mode.keySet()}");
        }

        def components = attrs.remove('components')
        if (components instanceof String) {
            components = components.split(/[,;]/).collect {it.trim()}
        }
        
        def javascripts = attrs.remove('javascript')
        if (javascripts instanceof String) {
            javascripts = javascripts.split(/[,;]/).collect {it.trim()}
        }
        
        def csses = attrs.remove('css')
        if (csses instanceof String) {
            csses = csses.split(/[,;]/).collect {it.trim()}
        }
        
        // if there are no components, no javascripts, and no css, there is no reason to have the tag, so throw
        if (!components && !javascripts && !csses) {
            throw new GrailsUIException("The resources tag must have either (components | javascript | css)")
        }

        // just for convenience
        def rollUps = config.rollUps

        def rolledTags = []
        def styleSheetList = [] as List
        def javascriptList = [] as List
        def flashList = [] as List
//        def bubblingList = [] as List
        
        // bundled check and add
        def attemptAddingToJavascriptList = {key ->
            if (!javascriptList.contains(key) && !rolledTags.contains(key)) {
                javascriptList << key
            }
        }
        
        components.each {component ->
            // gets the config for this tag
            def tagDefinition = config.tags."$component"

            // add this tag's stylesheet keys and javascript keys to the collection
            tagDefinition.styleSheetKeys.each {cssKey ->
                if (!styleSheetList.contains(cssKey)) {
                    styleSheetList << cssKey
                }
            }
            
            // process any javascript components
            tagDefinition.javascriptKeys.each {unprocessedJsKey ->
                // example: yahoo-dom-event is stored as yahoo_dom_event in the config
                def jsKey = unprocessedJsKey.replaceAll('-', '_')

                // for rolled up files
                if (rollUps.containsKey(jsKey)) {
                    // for minimal rollups, need to log the rolled up values and output one min file
                    if (mode == 'minimal') {
                        rollUps."${jsKey}".each {rolledVal ->
                            rolledTags << rolledVal
                        }
                        attemptAddingToJavascriptList(unprocessedJsKey)
                        // for non-minimal rollups, split up the rollup into individual files
                    } else {
                        rollUps."${jsKey}".each {rolledVal ->
                            attemptAddingToJavascriptList(rolledVal)
                            rolledTags << rolledVal
                        }
                    }
                    // normal, no rollups
                } else {
                    attemptAddingToJavascriptList(unprocessedJsKey)
                }
            }

            // the charts tags have a flash requirement
            tagDefinition.flashKeys.each { flashKey ->
                if (!(flashKey in flashList)) {
                    flashList << flashKey
                }
            }
        }
        
        // add any additional javascripts specified in the tag
        javascripts.each { javascript ->
            attemptAddingToJavascriptList(javascript)
        }
        
        // add any additional css files specified in the tag
        csses.each { css ->
            // must loop through all the available css within each resource tag
            config.resources.keySet().each { resource ->
                config.resources."$resource".css.each { cssKey, val ->
                    if (css == cssKey) {
                        styleSheetList << cssKey
                    }
                }
            }
        }

        // if components or a javascript include were involved in the tag, always include the grailsui.js file as the 
        // 2nd include, right after the base yahoo stuff
        if (components || javascriptList) {
            if (javascriptList.size() < 2) {
                javascriptList << 'grailsui'
            } else if (components) {
                javascriptList = (javascriptList[0..0] + 'grailsui' + javascriptList[1..-1]) as List
            }
        }

        styleSheetList.each {
            out << resourcesTagLibService.stylesheetTag(it, pluginContextPath, config)
        }

        javascriptList.each {
            out << resourcesTagLibService.javascriptTag(it, mode, pluginContextPath, config)
        }

        flashList.each {
            out << resourcesTagLibService.flashReference(it, pluginContextPath, config)
        }

/*        bubblingList.each {
            out << resourcesTagLibService.bubblingScriptTag(it, mode, pluginContextPath, config)
        }*/
    }
}
