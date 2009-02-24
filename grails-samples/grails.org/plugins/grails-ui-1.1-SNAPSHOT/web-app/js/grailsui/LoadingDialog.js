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
 GRAILSUI.LoadingDialog = function(oId, oText, oRenderTo) {
    var id = 'loading';
    if (arguments.length > 0) {
        id = oId;
    }
    var width = "50px";
    var height = "50px";
    var useHeader = oText != null && oText != undefined && oText != '';
    if (useHeader) {
        width = "auto";
        height = "80px";
    }
    var oConfig = {
        width:width,
        height:height,
        fixedcenter:true,
        close:false,
        draggable:false,
        zindex:4,
        modal:true,
        visible:false
    };
    GRAILSUI.LoadingDialog.superclass.constructor.call(this, oId, oConfig);
    if (useHeader) {
        this.setHeader(oText);
    }
    this.setBody("<div id='grailsuiLoading'></div>");
    if (oRenderTo != null) {
        this.render(oRenderTo);
    } else {
        this.render(document.body);
    }
};

YAHOO.lang.extend(GRAILSUI.LoadingDialog, YAHOO.widget.Panel);
