$('#jsi-nav').sidebar({
	trigger : '.jsc-sidebar-trigger',
	scrollbarDisplay : true,
	pullCb : function() {
		console.log('pull');
	},
	pushCb : function() {
		console.log('push');
	}
});

$('#api-push').on('click', function(e) {
	e.preventDefault();
	$('#jsi-nav').data('sidebar').push();
});
$('#api-pull').on('click', function(e) {
	e.preventDefault();
	$('#jsi-nav').data('sidebar').pull();
});