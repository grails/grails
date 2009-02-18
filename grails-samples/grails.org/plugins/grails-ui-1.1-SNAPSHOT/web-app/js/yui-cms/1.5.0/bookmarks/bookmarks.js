/*
Copyright (c) 2007, Caridy Patiño. All rights reserved.
Portions Copyright (c) 2007, Yahoo!, Inc. All rights reserved.
Code licensed under the BSD License:
http://www.bubbling-library.com/eng/licence
version: 1.5.0 (build:207)
*/
(function() {

    var $B = YAHOO.Bubbling,
  	    $Y = YAHOO.util,
	    $E = YAHOO.util.Event,
	    $D = YAHOO.util.Dom,
	    $T = YAHOO.plugin.Translator;
	    $ =  YAHOO.util.Dom.get;

	$T.load( 'bookmarks' );

    var GetListItemFromEventTarget = function (p_oNode) {
        var oLI;
        if(p_oNode.tagName == "LI") {
            oLI = p_oNode;
        }
        else {
            /*   If the target of the event was a child of an LI, get the parent LI element */
            do {
                if(p_oNode.tagName == "LI") {
                    oLI = p_oNode;
                    break;
                }
            }
            while((p_oNode = p_oNode.parentNode));
        }
        return oLI;
    };

	/**
	* @class BookmarkLinks
	* BookmarkLinks is an implementation of a Context Menu to link to the most importants Bookmark Systems on the WEB.
	* @constructor
	*/
	YAHOO.behavior.BookmarkLinks = function() {
		var obj = {};
		var menuClassName = "cms_bookmark_contextmenu";
		var properties = {
			actionBookmark: function (layer, args) {
			  var e = args[0],
				  o = args[1] || {},
				  el = args[1].anchor;
			  if (obj.oContextMenu) {
			  	obj.oContextMenu.clearContent ();
			  	obj.oContextMenu.destroy ();
			  }

			  // ----------------------------------
			  // Create the context menu
			  // ----------------------------------
			  if ($T.isReady('bookmarks')) {
			  	  // ----------------------------------------------
			      // Define the items for the context menu
			      // ----------------------------------------------
			      var aMenuItems = [
			        { text: 'CMO_BOOKMARK_MENEAME'.translate('bookmarks'), url:'http://meneame.net/submit.php?url={URI}', value:true, target: 'blank' },
			        { text: 'CMO_BOOKMARK_DIGG'.translate('bookmarks'), url:'http://www.digg.com/submit?url={URI}', value:true, target: 'blank' },
			        { text: 'CMO_BOOKMARK_DELICIOUS'.translate('bookmarks'), url:'http://del.icio.us/post?url={URI}&amp;title={TITLE}', value:true, target: 'blank' },
			        { text: 'CMO_BOOKMARK_TECHNORATI'.translate('bookmarks'), url:'http://technorati.com/faves?add={URI}', value:true, target: 'blank' },
			        { text: 'CMO_BOOKMARK_YAHOO'.translate('bookmarks'), url:'http://myweb2.search.yahoo.com/myresults/bookmarklet?u={URI}&amp;t={TITLE}', value:true, target: 'blank' },
			        { text: 'MCMS_CLOSE'.translate('bookmarks') }
			      ];

				  obj.oContextMenu = new YAHOO.widget.ContextMenu(
		                                menuClassName,
		                                {
		                                    trigger: el,
		                                    itemdata: aMenuItems,
		                                    lazyload: true,
											visible: false
		                                }
		                            );
				  // Add a "render" event handler to the AutoReader context menu
				  obj.oContextMenu.renderEvent.subscribe(onContextMenuRender, obj.oContextMenu, true);
				  obj.oContextMenu.render();
				  obj.oContextMenu.show();
				  obj.oContextMenu._onTriggerContextMenu( e, obj.oContextMenu );
				  // add the correct opacity value...
				  if (!$E.isIE || $B.force2alfa) {
				    $D.setStyle (obj.oContextMenu.element, 'opacity', 0.9);
				  }
				  // reclaiming the event and stop the propagation
	  			  return true;
			  }
			}
		};
		var actionControlContextMenus = function (layer, args) {
		  $B.processingAction (layer, args, properties);
		};
		$B.on('navigate', actionControlContextMenus);
		$B.on('property', actionControlContextMenus);

	    // "click" event handler for each item in the context menu
	    var onContextMenuClick = function (p_sType, p_aArgs, p_oMenu) {
	        var oItem = p_aArgs[1];
			obj.oContextMenu.hide();
	    };
	    // "processing" each element... to get the correct url...
		var onContextMenuCompile = function ( oItem ) {
			var rss = oItem.cfg.getProperty("url");
	        if(oItem && rss) {
				// proccesing the url...
				var t = $B.getFirstChildByTagName ( oItem.element, 'A' ),
					uri = oItem.parent.cfg.getProperty("trigger").href,
					title = oItem.parent.cfg.getProperty("trigger").title;
				t.setAttribute ('target', 'blank');
				if (oItem.value) {// if true, then apply the escape function for each params...
				  uri = escape(uri);
				  title = escape(title);
				}
				rss = rss.replace( new RegExp( "{URI}", "g" ), uri );
				rss = rss.replace( new RegExp( "{TITLE}", "g" ), title );
				oItem.cfg.setProperty("url", rss);
	        }
		};
	    // "render" event handler for the context menu
	    var onContextMenuRender = function (p_sType, p_aArgs, p_oMenu) {
			$D.addClass(this.getItem(0).element, 'cms_bm_meneame');
			$D.addClass(this.getItem(1).element, 'cms_bm_digg');
			$D.addClass(this.getItem(2).element, 'cms_bm_delicious');
			$D.addClass(this.getItem(3).element, 'cms_bm_technorati');
			$D.addClass(this.getItem(4).element, 'cms_bm_yahoo');
			$D.addClass(this.getItem(5).element, 'closemenu');
		    //  Add a "click" event handler to the context menu
		    this.clickEvent.subscribe(onContextMenuClick, this, true);
			$D.batch (this._getItemGroup(), onContextMenuCompile, this, true);
	    };
		// public vars
		obj.oContextMenu = null;
		// public methods
		return obj;
	}();
})();