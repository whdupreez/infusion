var snapper = new Snap({
	element: document.getElementById('content'),
	disable: 'right'
});

snapper.on('open', function(){
	document.getElementById('toggle-menu').style.display = "none";
});

snapper.on('close', function(){
	var delay = 200;
	setTimeout(function(){
		document.getElementById('toggle-menu').removeAttribute("style");
	}, delay);
});

var addEvent = function addEvent(element, eventName, func) {
	if (element.addEventListener) {
		return element.addEventListener(eventName, func, false);
	} else if (element.attachEvent) {
		return element.attachEvent("on" + eventName, func);
	}
};

addEvent(document.getElementById('open-left'), 'click', function(){
	snapper.open('left');
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

//snapper.open('left');

//$('#jsi-nav').sidebar({
//	trigger : '.jsc-sidebar-trigger',
//	scrollbarDisplay : true,
//	pullCb : function() {
//		console.log('pull');
//	},
//	pushCb : function() {
//		console.log('push');
//	}
//});
//
//$('#api-push').on('click', function(e) {
//	e.preventDefault();
//	$('#jsi-nav').data('sidebar').push();
//});
//$('#api-pull').on('click', function(e) {
//	e.preventDefault();
//	$('#jsi-nav').data('sidebar').pull();
//});