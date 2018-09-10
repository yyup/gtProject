(function($){'use strict';var defaultSettings={separator:true,ripple_effect:true,search_bar_hide:false,top_fixed:false,full_width:false,right_to_left:false,trigger:'click',vertical_tabs_trigger:'click',vertical_tabs_effect_speed:400,drop_down_effect_in_speed:200,drop_down_effect_out_speed:200,drop_down_effect_in_delay:200,drop_down_effect_out_delay:200,outside_close_dropDown:true,sticky_header:false,sticky_header_height:200,sticky_header_animation_speed:400,timer_on:false,timer_morning_color:'cyan',timer_afternoon_color:'red',timer_evening_color:'teal',internal_links_enable:true,internal_links_toggle_drop_down:false,internal_links_target_speed:400,mobile_search_bar_hide:false,mobile_sticky_header:false,mobile_sticky_header_height:100,media_query_max_width:768};$.fn.mashableMenu=function(settings){settings=$.extend({},defaultSettings,settings||{});return this.each(function(){var $this=$(this),$li='li',$a='a',active='active',Canvas,$object,$separatorShow='separator',list_items=$this.find('.mash-list-items'),drop_down=list_items.find('.drop-down, .drop-down-large, .drop-down-medium'),search_bar=$this.find('.mash-search-bar'),search_bar_hide='search-bar-hide',fixed='mash-top-fixed',full_width='mash-full-width',verticalContainer=drop_down.find('.vertical-tabs-container'),collapsible=drop_down.find('.collapsible'),tabsContainer=drop_down.find('.mash-tabs-container'),brand=$this.find('.mash-brand'),mobile_button=brand.find('.mash-mobile-button'),selectInput='.menu-select',verticalTabsContent=verticalContainer.find('.vertical-tabs-content'),mashTabs=tabsContainer.find('.mash-tabs'),mashTabsContent=tabsContainer.find('.mash-tabs-content'),collapsible_header=collapsible.find('.collapsible-header'),collapsible_body=collapsible.find('.collapsible-body'),right_to_left_class='right-to-left',contact_form=$this.find('.form-horizontal'),contact_form_notification=contact_form.find('.form-notifier'),dropDropOpen='.DropDownOpen';Canvas=(function(){Canvas=function Canvas(name){this.name=name;};Canvas.prototype.contact_form_ajax=function(){$(contact_form).submit(function(event){var current,get_form_data;current=$(this);event.preventDefault();get_form_data=$(this).serialize();current.find('button i.fa').css('display','inline-block');$.ajax({type:'POST',url:$(this).attr('action'),data:get_form_data}).done(function(response){contact_form_notification.text(response);current.find('input[type="text"]').val('');current.find('input[type="email"]').val('');current.find('textarea').val('');current.find('button i.fa').css('display','none');}).fail(function(data){if(data.responseText!==''){contact_form_notification.text('Error');}
current.find('button i.fa').css('display','none');});});};Canvas.prototype.collapsible_accordion=function(){collapsible.each(function(){var changer,collapse,elem;elem=$(this);changer=function(flag){if(flag===true){return elem.find(collapsible_body).slideUp(200);}};collapse=function(expand){elem.find(collapsible_header).off('click').on('click',function(current){current=$(this);if(current.siblings(collapsible_body).is(':visible')){current.siblings(collapsible_body).slideUp(200);}else{changer(expand);current.siblings(collapsible_body).slideDown(200);}
return false;});};if(elem.hasClass('collapsible-accordion')){collapse(true);}
if(elem.hasClass('collapsible-expandable')){collapse(false);}});};Canvas.prototype.verticalTabsResponsive=function(desktop){verticalContainer.each(function(){var elem,a_tag,a_active_content,verticalTabsClick,counter,content_container,a_clone;elem=$(this);a_tag=elem.find('.vertical-tabs > a');a_active_content=elem.find('.vertical-tabs > a.active').attr('href');content_container=elem.find('.vertical-tabs-content-container');if(desktop===true){elem.find('.vertical-tabs .vertical-tabs-content').remove();if(a_tag.hasClass(active)){$(a_active_content).show(0);}else{a_tag.eq(0).addClass(active);$(a_tag.eq(0).attr('href')).show(0);}
if(settings.vertical_tabs_trigger==='click'&&typeof settings.vertical_tabs_trigger==="string"){elem.on('click','.vertical-tabs > a',function(){return false;});verticalTabsClick=function(){var a_elem,current_a_active,current_a_href,current_a_tag;a_elem=$(this);current_a_active=elem.find('a.active');current_a_href=current_a_active.attr('href');if(!a_elem.hasClass(active)){a_tag.siblings($a).removeClass(active);a_elem.addClass(active);$(current_a_href).stop(true,true).fadeOut(settings.vertical_tabs_effect_speed,function(){current_a_tag=a_elem.attr('href');$(current_a_tag).stop(true,true).fadeIn(settings.vertical_tabs_effect_speed);elem.one('click','.vertical-tabs > a',verticalTabsClick);});}else{elem.one('click','.vertical-tabs > a',verticalTabsClick);}};elem.one('click','.vertical-tabs > a',verticalTabsClick);}
if(settings.vertical_tabs_trigger==='hover'&&typeof settings.vertical_tabs_trigger==="string"){elem.on('click','.vertical-tabs > a',function(){return false;});verticalTabsClick=function(){var a_elem,current_a_active,current_a_href,current_a_tag;a_elem=$(this);current_a_active=elem.find('a.active');current_a_href=current_a_active.attr('href');if(!a_elem.hasClass(active)){a_tag.siblings($a).removeClass(active);a_elem.addClass(active);$(current_a_href).stop(true,true).fadeOut(settings.vertical_tabs_effect_speed,function(){current_a_tag=a_elem.attr('href');$(current_a_tag).stop(true,true).fadeIn(settings.vertical_tabs_effect_speed);elem.one('mouseenter','.vertical-tabs > a',verticalTabsClick);});}else{elem.one('mouseenter','.vertical-tabs > a',verticalTabsClick);}};elem.one('mouseenter','.vertical-tabs > a',verticalTabsClick);}}else{if(a_tag.hasClass(active)){$(a_active_content).hide(0);}else{a_tag.eq(0).addClass(active);$(a_tag.eq(0).attr('href')).hide(0);}
if(content_container.find('> a').length===0){for(counter=0;counter<a_tag.length;counter+=1){a_clone=a_tag.eq(counter).clone();content_container.find('.vertical-tabs-content').eq(counter).before(a_clone);}}
elem.off('click').on('click','.vertical-tabs-content-container > a',function(event){event.stopPropagation();event.stopImmediatePropagation();event.preventDefault();var a_elem=$(this);a_elem.parent('.vertical-tabs-content-container').find($a).removeClass(active);a_elem.addClass(active);if(a_elem.next('.vertical-tabs-content').is(':hidden')){a_elem.parent('.vertical-tabs-content-container').find('.vertical-tabs-content').hide(0);a_elem.next('.vertical-tabs-content').show(0);}else{a_elem.next('.vertical-tabs-content').hide(0);}});}});};Canvas.prototype.drop_down_option_outside=function(){if(settings.internal_links_enable===true&&typeof settings.internal_links_enable==="boolean"){$(dropDropOpen).off('click').on('click',function(event){event.preventDefault();event.stopPropagation();event.stopImmediatePropagation();var elem,search,$href,$target;elem=list_items;search=search_bar;if(elem.is(':hidden')){elem.add(search).show(0);}
$href=$(this).attr('href');$target=$($href);if($target.is(':hidden')){$target.parents($li).siblings($li).find(drop_down).fadeOut(settings.drop_down_effect_out_speed);$target.parents(list_items).siblings(list_items).find(drop_down).fadeOut(settings.drop_down_effect_out_speed);$target.fadeIn(settings.drop_down_effect_in_speed);}else if(settings.internal_links_toggle_drop_down===true){$target.fadeOut(settings.drop_down_effect_out_speed);}
if(settings.internal_links_target_speed===0){settings.internal_links_target_speed=10;}
$('html, body').stop().animate({'scrollTop':$target.offset().top},settings.internal_links_target_speed);});}};Canvas.prototype.brand_close_dropDown=function(){brand.find($a).off('click').on('click',function(){drop_down.fadeOut(settings.drop_down_effect_out_speed);});};Canvas.prototype.outside_close=function(desktop){if(desktop===true&&settings.outside_close_dropDown===true&&typeof settings.outside_close_dropDown==="boolean"){$(window).on('click',function(event){if(!$this.is(event.target)&&$this.has(event.target).length===0){drop_down.fadeOut(settings.drop_down_effect_out_speed);}});}};Canvas.prototype.sibling_mobile=function(elem){if(elem.is(':hidden')){elem.parents($li).siblings($li).find(drop_down).fadeOut(0);elem.parents(list_items).siblings(list_items).find(drop_down).fadeOut(0);}else{elem.parent($li).find(drop_down).fadeOut(0);}};Canvas.prototype.sibling_desktop=function(elem){if(elem.is(':hidden')){elem.parents($li).siblings($li).find(drop_down).fadeOut(settings.drop_down_effect_out_speed);elem.parents(list_items).siblings(list_items).find(drop_down).fadeOut(settings.drop_down_effect_out_speed);}else{elem.parent($li).find(drop_down).fadeOut(settings.drop_down_effect_out_speed);}};Canvas.prototype.trigger_click=function(desktop){var current=this;if(desktop===true&&settings.trigger==='click'&&typeof settings.trigger==="string"){drop_down.prev($a).on('click',function(event){event.stopPropagation();event.stopImmediatePropagation();event.preventDefault();var elem=$(this).next(drop_down);if(elem.is(':hidden')){elem.delay(settings.drop_down_effect_in_delay).fadeIn(settings.drop_down_effect_in_speed);}else{elem.delay(settings.drop_down_effect_out_delay).fadeOut(settings.drop_down_effect_out_speed);}
current.sibling_desktop(elem);});}else if((desktop===false&&settings.trigger==='hover')||(desktop===false&&settings.trigger==='click')){drop_down.prev($a).on('click',function(event){event.stopPropagation();event.stopImmediatePropagation();var menu_width,target,elem;menu_width=drop_down.prev($a).innerWidth()/2;target=event.pageX;if(target>menu_width){event.preventDefault();}
elem=$(this).next(drop_down);if(elem.is(':hidden')){elem.delay(0).fadeIn(0);}else{elem.delay(0).fadeOut(0);}
current.sibling_mobile(elem);});}};Canvas.prototype.input_select=function(){$this.find(selectInput).dropdown({"autoinit":".menu-select"});};Canvas.prototype.trigger_hover=function(desktop){if(desktop===true&&settings.trigger==='hover'&&typeof settings.trigger==="string"){drop_down.parents($li).on({'mouseenter':function(){$(this).find(drop_down).first(drop_down).stop(true).delay(settings.drop_down_effect_in_delay).fadeIn(settings.drop_down_effect_in_speed);},'mouseleave':function(){$(this).find(drop_down).first(drop_down).stop(true).delay(settings.drop_down_effect_out_delay).fadeOut(settings.drop_down_effect_out_speed);}});}};Canvas.prototype.mobile_collapse_button=function(){mobile_button.off('click').on('click',function(event){event.stopPropagation();event.stopImmediatePropagation();event.preventDefault();var elem=list_items,search=search_bar;if(elem.is(':hidden')){elem.add(search).show(0);}else{elem.add(search).hide(0);}});};Canvas.prototype.stickyHeader=function(desktop){if(desktop===true&&settings.top_fixed===true&&typeof settings.top_fixed==="boolean"){$(window).off('scroll');return false;}
var expand=true,Scroll_height;if(desktop===true){Scroll_height=settings.sticky_header_height;}else{Scroll_height=settings.mobile_sticky_header_height;}
if((desktop===true&&settings.sticky_header===true)||(desktop===false&&settings.mobile_sticky_header===true)){$(window).off('scroll').on('scroll',function(){if($(window).scrollTop()>Scroll_height){if(expand===true){$this.fadeOut(settings.sticky_header_animation_speed,function(){$(this).addClass(fixed).fadeIn(settings.sticky_header_animation_speed);expand=false;});}}else{if(expand===false){$this.fadeOut(settings.sticky_header_animation_speed,function(){$(this).removeClass(fixed).fadeIn(settings.sticky_header_animation_speed);expand=true;});}}});}else{$(window).off('scroll');$this.removeClass(fixed);}};Canvas.prototype.menu_fullWidth=function(){if(settings.full_width===true&&typeof settings.full_width==="boolean"){$this.addClass(full_width);}else{$this.removeClass(full_width);}};Canvas.prototype.rightToLeft=function(){if(settings.right_to_left===true&&typeof settings.right_to_left==="boolean"){$this.addClass(right_to_left_class);}else{$this.removeClass(right_to_left_class);}};Canvas.prototype.menu_fixed=function(desktop){if(desktop===true&&settings.top_fixed===true&&typeof settings.top_fixed==="boolean"){$this.addClass(fixed);}else{$this.removeClass(fixed);}};Canvas.prototype.searchBar_hide=function(desktop){if(desktop===true&&settings.search_bar_hide===true&&typeof settings.search_bar_hide==="boolean"){search_bar.addClass(search_bar_hide);}else if(desktop===false&&settings.mobile_search_bar_hide===true&&typeof settings.mobile_search_bar_hide==="boolean"){search_bar.addClass(search_bar_hide);list_items.addClass(search_bar_hide);}else{search_bar.removeClass(search_bar_hide);list_items.removeClass(search_bar_hide);}};Canvas.prototype.google_ripple_effect=function(rippleEffect,buttons){if(settings.ripple_effect===true&&typeof settings.ripple_effect==="boolean"){rippleEffect=$this.find('.mash-brand > li > a,'+
'.mash-list-items > li > a,'+
'.drop-down > li > a,'+
'.btn,'+
'.collapsible-header,'+
'.vertical-tabs a,'+
'.nav.nav-tabs li a,'+
'.list-group a,'+
'.vertical-tabs-content-container > a,'+
'.mash-tabs-container .mash-tabs-mobile,'+
'.mash-tabs-container .mash-tabs a');buttons=drop_down.find('button, input');$.material.ripples(rippleEffect.add(buttons));}};Canvas.prototype.separator_show=function(){if(settings.separator===true&&typeof settings.separator==="boolean"){$this.addClass($separatorShow);}else{$this.removeClass($separatorShow);}};Canvas.prototype.color_change_time=function(now,hours,msg){if(settings.timer_on===true&&typeof settings.timer_on==="boolean"){now=new Date();hours=now.getHours();if(hours<12){msg=settings.timer_morning_color;}else if(hours<18){msg=settings.timer_afternoon_color;}else{msg=settings.timer_evening_color;}
$this.attr('data-color',msg);}};Canvas.prototype.destroy=function(){drop_down.parents($li).off('mouseenter mouseleave');drop_down.prev($a).off('click');$(window).off('click');verticalContainer.off('click mouseenter','.vertical-tabs > a');verticalTabsContent.hide(0);mashTabs.find('a').removeClass(active);mashTabs.find('a').off('click');mashTabsContent.find('>div').hide(0);};Canvas.prototype.mediaQuery=function(mediaQuery,current){current=this;mediaQuery=window.matchMedia("(max-width: "+settings.media_query_max_width+"px)");function media_query(property){if(property.matches){current.destroy();current.searchBar_hide(false);current.menu_fixed(false);current.stickyHeader(false);current.trigger_hover(false);current.trigger_click(false);current.outside_close(false);current.verticalTabsResponsive(false);}else{current.destroy();current.searchBar_hide(true);current.menu_fixed(true);current.stickyHeader(true);current.trigger_hover(true);current.trigger_click(true);current.outside_close(true);current.verticalTabsResponsive(true);}}
media_query(mediaQuery);mediaQuery.addListener(media_query);};return Canvas;}());$object=new Canvas();$object.mediaQuery();$object.color_change_time();$object.separator_show();$object.google_ripple_effect();$object.rightToLeft();$object.menu_fullWidth();$object.mobile_collapse_button();$object.input_select();$object.brand_close_dropDown();$object.drop_down_option_outside();$object.collapsible_accordion();$object.contact_form_ajax();});};}(jQuery));