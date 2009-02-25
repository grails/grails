/**
 * AJAXy Star-rating Script For Yahoo! UI Library (YUI)
 *
 * By <Ville@Unessa.net>
 * http://www.unessa.net/en/hoyci/projects/yui-star-rating/
 *
 * Based loosely on Wil Stuckeys jQuery Star Rating Plugin:
 * http://sandbox.wilstuckey.com/jquery-ratings/
 *
 * Respecting the original licence, this script is also
 * dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Version history
 * 0.1 - First version (2007-04-06)
 */

var rating = {
    init: function() {
        rating.ratingform = YAHOO.util.Dom.get('rating')
        rating.ratingdiv = YAHOO.util.Dom.get('ratingdiv')
        rating.stardiv = document.createElement('div')
        rating.notifytext = YAHOO.util.Dom.get('notifytext')
        rating.active = YAHOO.util.Dom.get('ratingIsActive').value
        rating.average = rating.ratingform.title.split(".")
        rating.submitted = false
        rating.make_stardiv()
    },

    make_stardiv: function() {
        /* Replaces original form with the star images */

        YAHOO.util.Dom.setStyle(rating.ratingform, 'display', 'none');
        YAHOO.util.Dom.addClass(rating.stardiv, 'rating');

        // make the stars
        for (var i=1; i<=5; i++) {
            // first, make a div and then an a-element in it
            var star = document.createElement('div');
            star.id = 'star' + i;
            var a = document.createElement('a');
            a.href = '#' + i;
            a.innerHTML = i;
            YAHOO.util.Dom.addClass(star, 'star');
            star.appendChild(a);
            rating.stardiv.appendChild(star);

            // add needed listeners to every star
            if (rating.active == 'true') {
                YAHOO.util.Event.addListener(star, 'mouseover', rating.hover_star, i);
                YAHOO.util.Event.addListener(star, 'mouseout', rating.reset_stars);
                YAHOO.util.Event.addListener(star, 'click', rating.submit_rating, i);
            }
        }
        rating.ratingdiv.appendChild(rating.stardiv);
        // show the average
        rating.reset_stars();
    },

    hover_star: function(e, which_star) {
        /* hovers the selected star plus every star before it */
        for (var i=1; i<=which_star; i++) {
            var star = YAHOO.util.Dom.get('star' + i);
            var a = star.firstChild;
            YAHOO.util.Dom.addClass(star, 'hover');
            YAHOO.util.Dom.setStyle(a, 'width', '100%');
        }
    },

    reset_stars: function() {
        /* Resets the status of each star */

        // if form is not submitted, the number of stars on depends on the
        // given average value
        if (rating.submitted == false) {
            var stars_on = rating.average[0];
            if (rating.average[1] >= 0)
                stars_on = parseInt(rating.average[0]) + 1;
            //var last_star_width = rating.average[1] + '0%';
            var last_start_width = '100%';
            if (rating.average[1] != undefined) {
                last_star_width = (parseFloat('0.' + rating.average[1]) * 100) + '%';
            }
        } else {
            // if the form is submitted, then submitted number stays on
            var stars_on = rating.submitted;
            var last_star_width = '100%';
        }

        // cycle trought 1..5 stars
        for (var i=1; i<=5; i++) {
            var star = YAHOO.util.Dom.get('star' + i);
            var a = star.firstChild;

            // first, reset all stars
            YAHOO.util.Dom.removeClass(star, 'hover');
            YAHOO.util.Dom.removeClass(star, 'on');

            // for every star that should be on, turn them on
            if (i<=stars_on && !YAHOO.util.Dom.hasClass(star, 'on'))
                YAHOO.util.Dom.addClass(star, 'on');

            // and for the last one, set width if needed
            if (i == stars_on)
                YAHOO.util.Dom.setStyle(a, 'width', last_star_width);
        }
    },

    submit_rating: function(e, num) {
        // If the form has not been submitted yet
        // and submission is not in progress
        if (rating.submitted == false) {
            rating.submitted = num;
            // After the form is submitted, instead of old average, show
            // submitted number of stars selected
            rating.average = [num, 0];

            // change the statustext div and show it
            rating.notifytext.innerHTML = 'Rating is being saved.';
//            var notify_display = new YAHOO.util.Anim(rating.notifytext, { opacity: { to: 1 } }, 0.25, YAHOO.util.Easing.easeIn);
//            notify_display.animate();

            // change the rating-value for the form and submit the form
            var post_to = rating.ratingform.action;
            rating.ratingform.elements[0].value = num;
            YAHOO.util.Connect.setForm(rating.ratingform);
            var c = YAHOO.util.Connect.asyncRequest('POST', post_to + '?xhr=True', rating.ajax_callback);
        }
    },

    ajax_callback: {
        success: function(o) {
            if (o.responseText.search('You are not currently logged in') >= 0) {
                rating.notifytext.innerHTML = 'Rating was not saved.';
                var text = 'You must Login before rating a plugin.<br/><a href="/login">Click here</a> to login.';
                YAHOO.util.Dom.get('loginFormDiv').innerHTML = text;
                GRAILSUI.loginDialog.show();
            } else {
                // release the form to normal status and change the statustext
                rating.submitted = false;
                var avg = o.responseText.split(',')[0];
                var total = o.responseText.split(',')[1];
                rating.notifytext.innerHTML = 'Rating saved. (' + total + ' ratings)';
                rating.average = avg.split(".");
                rating.ratingform.elements[0].value = parseInt(o.responseText);
                rating.reset_stars();
            }
        },
        failure: function(o) { // we shouldn't ever go down this path.
            alert('Error: ' + o.status + " " + o.statusText );
        }
    }
}
YAHOO.util.Event.addListener(window, 'load', rating.init);