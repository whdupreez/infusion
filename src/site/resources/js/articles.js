var snapper = new Snap({
	element: document.getElementById('content'),
	disable: 'right'
});

//snapper.on('open', function(){
//	document.getElementById('toggle-menu').style.display = "none";
//});
//
//snapper.on('close', function() {
//	var delay = 200;
//	setTimeout(function(){
//		document.getElementById('toggle-menu').removeAttribute("style");
//	}, delay);
//});

var addEvent = function addEvent(element, eventName, func) {
	if (element.addEventListener) {
		return element.addEventListener(eventName, func, false);
	} else if (element.attachEvent) {
		return element.attachEvent("on" + eventName, func);
	}
};

addEvent(document.getElementById('open-left'), 'click', function(){
	if (snapper.state().state=="left") {
        snapper.close();
    } else {
        snapper.open('left');
    }
});

/* Prevent Safari opening links when viewing as a Mobile App */
(function (a, b, c) {
	if(c in b && b[c]) {
		var d, e = a.location,
			f = /^(a|html)$/i;
		a.addEventListener("click", function (a) {
			d = a.target;
			while(!f.test(d.nodeName)) d = d.parentNode;
			"href" in d && (d.href.indexOf("http") || ~d.href.indexOf(e.host)) && (a.preventDefault(), e.href = d.href)
		}, !1)
	}
})(document, window.navigator, "standalone");
