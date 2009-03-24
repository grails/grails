/**
 * @author Graeme Rocher
 * @since 1.0
 * 
 * Created: Oct 31, 2007
 */
package org.grails.doc.filters

import org.radeox.regex.MatchResult
import org.radeox.filter.context.FilterContext

class ListFilter extends org.radeox.filter.ListFilter{

    public void handleMatch(StringBuffer buffer, MatchResult result, FilterContext context) {
        super.handleMatch(buffer, result, context);
        buffer << "\n\n"
    }



}