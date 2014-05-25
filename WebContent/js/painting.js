function paintStart() {
	$('#tabs').find('ul.tree').each(function() {
		var $ul = $(this);	
		var ret = paintUl($ul);
	});
}

function paintUl($parent){
	var toCompare;
	var li = $parent.children('li');
	if(li.length!=0){
		toCompare = paintLi($(li[0]));
		for(var i = 1, j = li.length; i < j; ++i) {
			var $li = $(li[i]);	
			if(paintLi($li) != toCompare)
				toCompare = 1;
		}
		return toCompare;
	}
	return -1;
}
function paintLi($parent) {
	var $a = $parent.children('a');
	var $ul = $parent.children('ul');
	var color = paintUl($ul);
	switch (color) {
	case 0:
		clearClass($a);
		$a.addClass('notappointed'); break;
	case 1:
		clearClass($a);
		$a.addClass('semiappointed'); break;
	case 2:
		clearClass($a);
		$a.addClass('appointed'); break;
	case -1:
		if($a.hasClass('appointed'))
			return 2;
		else 
			return 0;
	}
	$parent.attr('color', color);
	return color;
}

function clearClass($a){
	$a.removeClass('notappointed')
	.removeClass('semiappointed')
	.removeClass('appointed');		
}

$(function(){	
	paintStart();
});