function SelCity(obj,e,otherObj) {
    var ths = obj;
    var otherThs=otherObj;
    var dal = '<div class="_citys"><span title="关闭" id="cColse" >×</span><ul id="_citysheng" class="_citys0"><li class="citySel">Province</li><li>City</li><li>Country</li></ul><div id="_citys0" class="_citys1"></div><div style="display:none" id="_citys1" class="_citys1"></div><div style="display:none" id="_citys2" class="_citys1"></div></div>';
    Iput.show({ id: ths, event: e, content: dal,width:"470"});
    $("#cColse").click(function () {
        Iput.colse();
    });
    var tb_province = [];
    var b = province;
    for (var i = 0, len = b.length; i < len; i++) {
    	var small_province=b[i]['name'];
    	var first_letter=small_province.substr(0,1);
    	var big_province=first_letter.toUpperCase()+small_province.substr(1,small_province.length);
    	if(big_province.indexOf("sheng")>0){
    		var sheng=big_province.substr(big_province.length-5,big_province.length);
    		if(sheng=="sheng"){
    			big_province=big_province.substr(0,big_province.length-5);
    		}
    	}
        tb_province.push('<a data-level="0" data-id="' + b[i]['id'] + '" data-name="' + big_province + '">' + big_province + '</a>');
    }
    $("#_citys0").append(tb_province.join(""));
    $("#_citys0 a").click(function () {
        var g = getCity($(this));
        $("#_citys1 a").remove();
        $("#_citys1").append(g);
        $("._citys1").hide();
        $("._citys1:eq(1)").show();
        $("#_citys0 a,#_citys1 a,#_citys2 a").removeClass("AreaS");
        $(this).addClass("AreaS");
        var lev = $(this).data("name");
        ths.value = $(this).data("name");
        otherThs.value = $(this).data("name");
        $(ths).attr("provinceId",$(this).data("id"));
        $(otherThs).attr("provinceId",$(this).data("id"));
        if (document.getElementById("hcity") == null) {
            var hcitys = $('<input>', {
                type: 'hidden',
                name: "hcity",
                "data-id": $(this).data("id"),
                id: "hcity",
                val: lev
            });
            $(ths).after(hcitys);
            $(otherThs).after(hcitys);
            
        }
        else {
            $("#hcity").val(lev);
            $("#hcity").attr("data-id", $(this).data("id"));
        }
        $("#_citys1 a").click(function () {
            $("#_citys1 a,#_citys2 a").removeClass("AreaS");
            $(this).addClass("AreaS");
            var lev =  $(this).data("name");
            if (document.getElementById("hproper") == null) {
                var hcitys = $('<input>', {
                    type: 'hidden',
                    name: "hproper",
                    "data-id": $(this).data("id"),
                    id: "hproper",
                    val: lev
                });
                $(ths).after(hcitys);
                $(otherThs).after(hcitys);
                
            }
            else {
                $("#hproper").attr("data-id", $(this).data("id"));
                $("#hproper").val(lev);
            }
            var bc = $("#hcity").val();;
            ths.value = bc+ "-" + $(this).data("name");
            otherThs.value = bc+ "-" + $(this).data("name");
            $(ths).attr("cityId",$(this).data("id"));
            $(otherThs).attr("cityId",$(this).data("id"));
            var ar = getArea($(this));

            $("#_citys2 a").remove();
            $("#_citys2").append(ar);
            $("._citys1").hide();
            $("._citys1:eq(2)").show();

            $("#_citys2 a").click(function () {
                $("#_citys2 a").removeClass("AreaS");
                $(this).addClass("AreaS");
                var lev = $(this).data("name");
                if (document.getElementById("harea") == null) {
                    var hcitys = $('<input>', {
                        type: 'hidden',
                        name: "harea",
                        "data-id": $(this).data("id"),
                        id: "harea",
                        val: lev
                    });
                    $(ths).after(hcitys)
                    $(otherThs).after(hcitys);;
                }
                else {
                    $("#harea").val(lev);
                    $("#harea").attr("data-id", $(this).data("id"));
                }
                var bc = $("#hcity").val();
                var bp = $("#hproper").val();
                ths.value = bc + "-" + bp + "-" + $(this).data("name");
                otherThs.value = bc + "-" + bp + "-" + $(this).data("name");
                $(ths).attr("areaId",$(this).data("id"));
                $(otherThs).attr("areaId",$(this).data("id"));
                Iput.colse();
            });

        });
    });
    $("#_citysheng li").click(function () {
        $("#_citysheng li").removeClass("citySel");
        $(this).addClass("citySel");
        var s = $("#_citysheng li").index(this);
        $("._citys1").hide();
        $("._citys1:eq(" + s + ")").show();
    });
}

function getCity(obj) {
    var c = obj.data('id');
    var e = province;
    var f;
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['id'] == c) {
            f = e[i]['city'];
            break
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++) {
    	var small_city=f[j]['name'];
    	var first_letter=small_city.substr(0,1);
    	var big_city=first_letter.toUpperCase()+small_city.substr(1,small_city.length);
    	if(big_city.indexOf("shi")>0){
    		var shi=big_city.substr(big_city.length-3,big_city.length);
    		if(shi=="shi"){
    			big_city=big_city.substr(0,big_city.length-3);
    		}
    	}
    	
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + big_city + '" title="' + big_city + '">' + big_city + '</a>';
    }
    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(1)").addClass("citySel");
    return g;
}
function getArea(obj) {
    var c = obj.data('id');
    var e = area;
    var f = [];
    var g = '';
    for (var i = 0, plen = e.length; i < plen; i++) {
        if (e[i]['pid'] == c) {
            f.push(e[i]);
        }
    }
    for (var j = 0, clen = f.length; j < clen; j++) {
    	var small_area=f[j]['name'];
    	var first_letter=small_area.substr(0,1);
    	var big_area=first_letter.toUpperCase()+small_area.substr(1,small_area.length);
    	if(big_area.indexOf("shi")>0){    
    		var shi=big_area.substr(big_area.length-3,big_area.length);
    		if(shi=="shi"){
    			big_area=big_area.substr(0,big_area.length-3);
    		}
    	}else if(big_area.indexOf("qu")>0){    		
    		var qu=big_area.substr(big_area.length-2,big_area.length);
    		if(qu=="qu"){
    			big_area=big_area.substr(0,big_area.length-2);
    		}
    	}else if(big_area.indexOf("xian")>0){  
    		var xian=big_area.substr(big_area.length-4,big_area.length);
    		if(xian=="xian"){
    			big_area=big_area.substr(0,big_area.length-4);
    		}
    	}
        g += '<a data-level="1" data-id="' + f[j]['id'] + '" data-name="' + big_area + '" title="' + big_area + '">' + big_area + '</a>';
    }

    $("#_citysheng li").removeClass("citySel");
    $("#_citysheng li:eq(2)").addClass("citySel");
    return g;
}