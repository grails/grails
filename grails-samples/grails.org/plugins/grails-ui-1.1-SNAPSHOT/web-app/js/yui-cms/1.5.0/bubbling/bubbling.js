/*
Copyright (c) 2007, Caridy Patiño. All rights reserved.
Portions Copyright (c) 2007, Yahoo!, Inc. All rights reserved.
Code licensed under the BSD License:
http://www.bubbling-library.com/eng/licence
version: 1.5.0
*/
YAHOO.namespace("plugin","behavior");
(function() {
  var $Y = YAHOO.util,
	  $E = YAHOO.util.Event,
	  $D = YAHOO.util.Dom,
	  $L = YAHOO.lang,
	  $  = YAHOO.util.Dom.get;

  /**
  * @class Bubbling
  */
  YAHOO.Bubbling = function () {
  	var obj = {},
		ua = navigator.userAgent.toLowerCase(),
        isOpera = (ua.indexOf('opera') > -1);
	// private stuff
	var navRelExternal = function (layer, args) {
		  var el = args[1].anchor;
		  if (!(args[1].flagged || args[1].decrepitate) && el) {
			  var r = el.getAttribute("rel"),
			  	  t = el.getAttribute("target");
		  	  if ((!t || (t === '')) && (r == 'external')) {
				el.setAttribute("target", "blank");
		      }
		  }
	};
    var defaultActionsControl = function (layer, args) {
	  obj.processingAction (layer, args, obj.defaultActions);
    };
	var _searchYUIButton = function (t) {
		var el = obj.getOwnerByClassName( t, 'yui-button' ), bt = null, id = null;
		if ($L.isObject(el) && YAHOO.widget.Button) {
			bt = YAHOO.widget.Button.getButton(el.id);
		}
		return bt;
	};

	// public vars
	obj.ready = false;
	obj.force2alfa = false;
	obj.bubble = {}; // CustomEvent Handles
    obj.onReady = new $Y.CustomEvent('bubblingOnReady', obj, true);

	// mapping external methods...
	obj.getOwnerByClassName = function(node, className) {
		return ($D.hasClass(node, className)?node:$D.getAncestorByClassName (node, className));
    };
    obj.getOwnerByTagName = function(node, tagName) {
		node = $D.get(node);
		if (!node) {
			return null;
		}
		return (node.tagName && node.tagName.toUpperCase() == tagName.toUpperCase()?node:$D.getAncestorByTagName (node, tagName));
	};
	// Deprecated in favor of getOwnerByClassName and getOwnerByTagName
	obj.getAncestorByClassName = obj.getOwnerByClassName;
    obj.getAncestorByTagName = obj.getOwnerByTagName;

	// public methods
	obj.onKeyPressedTrigger = function(args, e, m){
	  var b = 'key';
	  e = e || $E.getEvent();
	  m = m || {};
	  m.action = b;
	  m.target = args.target || (e?$E.getTarget(e):null);
	  m.flagged = false; m.decrepitate = false;
	  m.event = e;
	  m.stop = false;
	  m.type = args.type;
	  m.keyCode = args.keyCode;
	  m.charCode = args.charCode;
	  m.ctrlKey = args.ctrlKey;
	  m.shiftKey = args.shiftKey;
	  m.altKey = args.altKey;
	  this.bubble.key.fire(e, m);
	  if (m.stop) {
	  	$E.stopEvent(e);
	  }
	  return m.stop;
	};
	obj.onEventTrigger = function(b, e, m){
	  e = e || $E.getEvent();
	  m = m || {};
	  m.action = b;
	  m.target = (e?$E.getTarget(e):null);
	  m.flagged = false; m.decrepitate = false;
	  m.event = e;
	  m.stop = false;
	  this.bubble[b].fire(e, m);
	  if (m.stop) {
	  	$E.stopEvent(e);
	  }
	  return m.stop;
	};
	obj.onNavigate = function(e){
	  var conf = {
	  	anchor: this.getOwnerByTagName( $E.getTarget(e), 'A' ),
		button: _searchYUIButton($E.getTarget(e))
	  };
	  if (!conf.anchor && !conf.button) {
	  	conf.input = this.getOwnerByTagName( $E.getTarget(e), 'INPUT' );
	  }
	  if (conf.button) {
            conf.value = conf.button.get('value');
	  } else if (conf.input) {
	        conf.value = conf.input.getAttribute('value');
	  }
	  if (!this.onEventTrigger ('navigate', e, conf)) {
	    this.onEventTrigger ('god', e, conf); // if nobody claim the event, god can handle it...
	  }
	};
	obj.onProperty = function(e){
	  this.onEventTrigger ('property', e, {
	  	anchor: this.getOwnerByTagName( $E.getTarget(e), 'A' ),
		button: _searchYUIButton($E.getTarget(e))
	  });
	};
	obj._timeoutId = 0;
	obj.onRepaint = function(e){
	  // Downshift Your Code (can’t let something happen multiple times in a second)
	  // http://yuiblog.com/blog/2007/07/09/downshift-your-code/
      clearTimeout(obj._timeoutId);
      obj._timeoutId = setTimeout(function(){
            var b = 'repaint',
                e = {target:document.body},
                m = {
            	    action: b,
            	    target: null,
            	    event: e,
	  				flagged: false,
            	    decrepitate: false,
            	    stop: false
            	};
        	obj.bubble[b].fire(e, m);
        	if (m.stop) {
        	  	$E.stopEvent(e);
        	}
        }, 150
      );
	};
	obj.onRollOver = function(e){
	  this.onEventTrigger ('rollover', e, {
	  	anchor: this.getOwnerByTagName( $E.getTarget(e), 'A' )
	  });
	};
	obj.onRollOut = function(e){
	  this.onEventTrigger ('rollout', e, {
	  	anchor: this.getOwnerByTagName( $E.getTarget(e), 'A' )
	  });
	};
	obj.onKeyPressed = function(args){
	  this.onKeyPressedTrigger(args);
	};
	/**
	* * Este método determina la acción por defecto para un elemento
	* @public
	* @param {object} el        element reference
	* @param {object} actions   object with the list of posibles actions
	* @return void
	*/
	obj.getActionName = function (el, depot) {
	  depot = depot || {};
	  var b = null, r = null,
	      f = ($D.inDocument(el)?function(b){return $D.hasClass(el, b)}:function(b){return el.hasClass(b);}); // f: check is certain object has a classname
	  if (el && ($L.isObject(el) || (el = $( el )))) {
	  	try{
			r = el.getAttribute("rel"); // if rel is available...
		}catch(e){};
		for (b in depot) { // behaviors in the depot...
			if ((depot.hasOwnProperty(b)) && (f(b) || (b === r))) {
				return b;
			}
		}
	  }
	  return null;
	};
	/**
	* * Este método determina el primer tab hijo basado en el tabName
	* @public
	* @param {object} el Child element reference
	* @param {object} c  ClassName of the Ancestor
	* @return void
	*/
	obj.getFirstChildByTagName = function (el, t) {
	  if (el && ($L.isObject(el) || (el = $( el ))) && t) {
	  	var l = el.getElementsByTagName(t);
		if (l.length > 0) {
		  return l[0];
		}
	  }
	  return null;
	};
	/**
	* * Este método determina si un evento es interno o no a un contenedor...
	* @public
	* @param {object} e  Referencia al evento
	* @param {object} el Referencia al contendor
	* @return void
	*/
	obj.virtualTarget = function (e, el) {
	  if (el && ($L.isObject(el) || (el = $( el ))) && $L.isObject(e)) {
	    var t = $E.getRelatedTarget ( e );  // target element
	    if ($L.isObject(t)) {
	      while((t.parentNode) && $L.isObject(t.parentNode) &&  (t.parentNode.tagName !== "BODY")) {
		    if (t.parentNode === el) {
		      return true;
		    }
		    t = t.parentNode;
		  }
		}
	  }
	  return false;
	};

	/**
	* * Creating a new behaviors layer...
	* @public
	* @param {string||array} layers  Behaviors layers GUID
	* @param {object} scope  Custom  Event default execution scope
	* @return boolean if not exists...
	*/
    obj.addLayer = function (layers, scope) {
		var result = false;
		layers = ($L.isArray(layers)?layers:[layers]);
        scope = scope || window;
		for (var i = 0; i < layers.length; ++i) {
          if (layers[i] && !this.bubble.hasOwnProperty(layers[i])) {
            this.bubble[layers[i]] = new $Y.CustomEvent(layers[i], scope, true);
            result = true;
          }
        }
		return result;
    };
	/**
	* * Subcribing an bahavior to certain bahaviors layer...
	* @public
	* @param {string} layer  Behavior layer GUID
	* @param {object} bh     The function that represent the behavior
	* @return boolean if it is the first listener
	*/
    obj.subscribe = function (layer, bh, scope) {
        var first = this.addLayer(layer); // return true if it's the first listener
        if (layer) {
			if ($L.isObject(scope)) {
			  this.bubble[layer].subscribe(bh, scope, true);  // correcting the default scope
			} else {
			  this.bubble[layer].subscribe(bh);  // use the default scope
			}
        }
        return first;
    };
    obj.on = obj.subscribe; // defining an alias...
	/**
	* * Broadcasting the message in the corresponding behavior layer...
	* @public
	* @param {string} layer  Behavior layer GUID
	* @param {object} obj    The function that represent the behavior
	* @return boolean if someone has claim the event
	*/
    obj.fire = function (layer, obj) {
	    obj = obj || {};
	    obj.action = layer;
	    obj.flagged = false; obj.decrepitate = false;
	    obj.stop = false;
	    if (this.bubble.hasOwnProperty(layer)) {
	        this.bubble[layer].fire(null, obj);
	    }
	    return obj.stop;
    };
	/**
	* * Processing an action based on the classname of the target element...
	* @public
	* @param {string} layer     Behavior layer GUID
	* @param {object} args      Event object (extended)
	* @param {object} actions   List of availables behaviors...
	* @param {boolean} force    Proccess the actions without worry about the flagged value...
	* @return void
	*/
	obj.processingAction = function (layer, args, actions, force) {
      var behavior = null, t;
	  if (!(args[1].flagged || args[1].decrepitate) || force) {
	  	  // checking for anchor, input or button
		  t = args[1].anchor || args[1].input || args[1].button;
		  if (t) {
			behavior = this.getActionName ( t, actions );
			args[1].el = t;
		  }
		  if (behavior && (actions[behavior].apply(args[1], [layer, args]))) {
			$E.stopEvent(args[0]);
			args[1].flagged = true;
		    args[1].decrepitate = true;
		    args[1].stop = true;
		  }
	  }
	};
	obj.defaultActions = {};
    obj.addDefaultAction = function (n, f, force) {
		if (n && f && (!this.defaultActions.hasOwnProperty(n) || force)) {
		  	this.defaultActions[n] = f;
		}
    };

	// default behaviors
	$E.addListener(window, "resize", obj.onRepaint, obj, true);

	// default Suscriptions
	obj.on('navigate', navRelExternal);
	obj.on('navigate', defaultActionsControl);

	// initialization of the font and scroll monitors
	obj.initMonitors = function ( config ) {
	    var fMonitors = function () {
            var oMonitors = new YAHOO.widget.Module('yui-cms-font-monitor', {
                monitorresize:true,
                visible:false
            });
            oMonitors.render(document.body);
            // monitoring font-size...
            YAHOO.widget.Module.textResizeEvent.subscribe(obj.onRepaint, obj, true);
            // monitoring scroll actions...
            YAHOO.widget.Overlay.windowScrollEvent.subscribe(obj.onRepaint, obj, true);
	    };
	    if ($L.isFunction(YAHOO.widget.Module)) {
	      $E.onDOMReady (fMonitors, obj, true);
	    }
    };
	// initialization inside the selfconstructor
	obj.init = function () {
	  if (!this.ready) {
	    var el = document.body;
	    $E.addListener(el,
			"click",
			obj.onNavigate,
			obj,
			true
		);
	    /*
	        Listen for the "mousedown" event in Opera b/c it does not
	        support the "contextmenu" event
	    */
	    $E.addListener(
	        el,
	        (isOpera ? "mousedown" : "contextmenu"),
	        obj.onProperty,
	        obj,
	        true
	    );
	    /*
	        Assign a "click" event handler to the trigger element(s) for
	        Opera to prevent default browser behaviors.
	    */
	    if(isOpera) {
	        $E.addListener(
	            el,
	            "click",
	            obj.onProperty,
	            obj,
	            true
	        );
	    }
		$E.addListener(el, "mouseover", obj.onRollOver, obj, true);
	    $E.addListener(el, "mouseout", obj.onRollOut, obj, true);

		// keys...
        $E.addListener(document, "keyup",  obj.onKeyPressed, obj, true);
		$E.addListener(document, "keydown",  obj.onKeyPressed, obj, true);

		this.ready = true;
		obj.onReady.fire();
	  }
	};
	$E.onDOMReady(obj.init, obj, true);

	// creating the default layers...
	obj.addLayer (['navigate','god','property','key','repaint','rollover', 'rollout']); // god layer - hack: the layer after the common navigate layer...

	return obj;
  }();
})();
YAHOO.register("bubbling", YAHOO.Bubbling, {version: "1.5.0", build: "222"});