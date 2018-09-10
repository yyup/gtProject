package com.gt.wl.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTML相关的正则表达式工具类
 * 包括过滤HTML标记，转换HTML标记，替换特定HTML标记
 * @author liuyj
 */

public class HtmlRegexpUtil {
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
	private final static String regxpForImgTag = "<[url=file://s*img//s+([%5E%3E]*)//s]\\s*img\\s+([^>]*)\\s[/url]*>"; // 找出IMG标签
	private final static String regxpForImaTagSrcAttrib = "src=\"([^\"]+)\""; // 找出IMG标签的SRC属性

	/**
	 * 替换标记以正常显示
	 * @param input 源字符串
	 * @return String 替换后字符串
	 */
	public static String replaceTag(String input) {
		if (!hasSpecialChars(input)) {
			return input;
		}
		StringBuffer filtered = new StringBuffer(input.length());
		char c;
		for (int i = 0; i <= input.length() - 1; i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}
		}
		return (filtered.toString());
	}

	/**
	 * 
	 * 基本功能：判断标记是否存在	
	 * @param input 源字符串
	 * @return boolean 是true 否false
	 */
	public static boolean hasSpecialChars(String input) {
		boolean flag = false;
		if ((input != null) && (input.length() > 0)) {
			char c;
			for (int i = 0; i <= input.length() - 1; i++) {
				c = input.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * 
	 * 基本功能：过滤所有以"<"开头以">"结尾的标签
	 * @param str html源码
	 * @return String
	 */
	public static String filterHtml(String str) {
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * 基本功能：过滤指定标签	
	 * @param str html源码
	 * @param tag 指定标签
	 * @return String
	 */
	public static String fiterHtmlTag(String str, String tag) {
		String regxp = "<[url=file://s/]\\s[/url]*" + tag + "[url=file://s+([%5e%3e]*)//s]\\s+([^>]*)\\s[/url]*>";
		Pattern pattern = Pattern.compile(regxp);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
			matcher.appendReplacement(sb, "");
			result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 
	 * 基本功能：替换指定的标签
	 * @param str html源码
	 * @param beforeTag 要替换的标签
	 * @param tagAttrib 要替换的标签属性值
	 * @param startTag  新标签开始标记
	 * @param endTag  新标签结束标记
	 * @return String
	 * @如：替换img标签的src属性值为[img]属性值[/img]
	 */
	public static String replaceHtmlTag(String str, String beforeTag, String tagAttrib, String startTag, String endTag) {
		String regxpForTag = "<[url=file://s/]\\s[/url]*" + beforeTag + "[url=file://s+([%5e%3e]*)//s]\\s+([^>]*)\\s[/url]*>";
		String regxpForTagAttrib = tagAttrib + "=\"([^\"]+)\"";
		Pattern patternForTag = Pattern.compile(regxpForTag);
		Pattern patternForAttrib = Pattern.compile(regxpForTagAttrib);
		Matcher matcherForTag = patternForTag.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result = matcherForTag.find();
		while (result) {
			StringBuffer sbreplace = new StringBuffer();
			Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
			if (matcherForAttrib.find()) {
				matcherForAttrib.appendReplacement(sbreplace, startTag + matcherForAttrib.group(1) + endTag);
			}
			matcherForTag.appendReplacement(sb, sbreplace.toString());
			result = matcherForTag.find();
		}
		matcherForTag.appendTail(sb);
		return sb.toString();
	}
	
	public static void main(String[] args){
		String str="<p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><br /></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\"><span style=\"font-size:large;\">11月5日，国内顶级的商界跑步盛会&mdash;&mdash;总裁跑Boss Run再战上海滩，WaterRower（沃特罗伦）作为Boss Run顶级合作伙伴，全程倾情助力，为总裁跑比赛盛事增添了全新体验，燃爆了全场。</span><span style=\"font-face:微软雅黑;\">云麦科技、天马集团、碧生源、固金所等</span><span style=\"font-size:large;\">30多家企业的精英团队在企业家带领下，用速度与激情，演绎了团结、拼搏与荣耀的跨界之役。</span></span></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\"><span style=\"font-size:large;\"><br /></span></span></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;text-indent:21pt;\">2017年是世界水划船机诞生三十周年，WaterRower全球CEO Peter King特地为Boss Run现场发来了祝福视频， WaterRower英国、德国、瑞典、西班牙、台湾等遍布世界的分公司也纷纷发来祝福：&ldquo;祝WaterRower三十周年生日快乐！&rdquo;</span></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><br /></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;text-indent:21pt;\">WaterRower中国区总经理谢咏霖现身Boss Run现场，为总裁和精英们带来了水划船机的故事：1987年，耶鲁大学和前美国国家队队员约翰&middot;杜克（John Duke）发明了第一台水划船机，并获发明专利，由此诞生了WaterRower品牌。&rdquo;water rower&rdquo;这个词本身就是英文水划船机的意思，因此WaterRower不仅是品牌名，更是水划船机的代名词。</span></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;text-indent:21pt;\"></span><br /></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"><span style=\"font-size:large;\"><img alt=\"\" src=\"http://www.waterrower.com.cn/wl/wl/cm/wlCmDocumentAction.dox?action=downloadFile&id=297ebc2d5fbd3463015fbd9cb80d0028\" style=\"display:block;margin-left:auto;margin-right:auto;\" /></span></span><br /></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\"><span style=\"font-face:微软雅黑;\">不是装了水的划船机，就叫</span><span style=\"font-size:large;\">WaterRower。&rdquo;谢总说。</span></span><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"> </span><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\">&nbsp; &nbsp; &nbsp;<br /></span><br /></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\">在Boss Run会场的VIP区，还展出了1988年产的世界第一代水划船机WaterRower Series 1，十分难得。<br /><br /></span></p><p style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:7.8000pt;text-indent:21.0000pt;padding:0pt 0pt 0pt 0pt;\"><span style=\"text-align:justify;text-indent:0pt;font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\">活动当天， WaterRower的品牌展示区热闹非凡，许多运动爱好者特来体验水划船机，连小朋友们也在教练的指导下纷纷尝试。袁岳等多位重磅级企业家在现场划起了WaterRower，尽情感受陆地赛艇的乐趣。</span><span style=\"text-align:justify;text-indent:0pt;font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"> </span><span style=\"text-align:justify;text-indent:0pt;font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\">&nbsp;<br /></span><br /></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:large;\"></span><img alt=\"\" src=\"http://www.waterrower.com.cn/wl/wl/cm/wlCmDocumentAction.dox?action=downloadFile&id=297ebc2d5fbd3463015fbd9f15c8002f\" style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:normal;text-align:justify;display:block;margin-left:auto;margin-right:auto;\" /></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"><span style=\"font-size:large;\"><br /></span></span></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:normal;text-align:justify;\">&nbsp; &nbsp; &nbsp;&ldquo;WaterRower水划船机500米竞速&rdquo;项目是此次比赛的亮点之一。通过该项目的比拼可为团队赢得奖励时间从而直接影响团队总成绩，因而备受关注。WaterRower上海指定线下体验中心FHIIT LAB的专业教练全程指导选手动作，企业总裁与精英们在加油声与WaterRower的水声中感受了一把水上赛艇般的刺激与畅快。</span><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"><span style=\"font-size:large;\"></span></span></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"><span style=\"font-size:large;\"><br /></span></span></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><img alt=\"\" src=\"http://www.waterrower.com.cn/wl/wl/cm/wlCmDocumentAction.dox?action=downloadFile&id=297ebc2d5fbd3463015fbd9d2f200029\" style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:normal;text-align:justify;display:block;margin-left:auto;margin-right:auto;\" /></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"font-family:微软雅黑;color:#0f0f0f;letter-spacing:0pt;font-size:10.5pt;\"><span style=\"font-size:large;\"></span></span><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;\"><br />&nbsp; &nbsp; &nbsp; &nbsp;比赛计分使用了专为WaterRower设计的APP &ldquo;iRow&rdquo;，这也是&ldquo;iRow&rdquo;的第一次亮相，配合蓝牙模块，每一台WaterRower均能够精确记录运动数据、进行线上互动。</span></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;\"><br /></span></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;text-indent:0pt;\">&nbsp; &nbsp; &nbsp; &nbsp; 本次Boss Run奖品可谓土豪：魅力总裁奖、水划船机比赛第一名团队均获赠价值&yen;17,198的WaterRower总统款水划船机一台，WaterRower中国区总经理谢咏霖先生亲自为获奖者颁奖。</span></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"color:#0f0f0f;font-family:微软雅黑;font-size:large;letter-spacing:0pt;text-indent:0pt;\"><br /></span><br /></p><p align=\"justify\" style=\"margin-top:0.0000pt;margin-right:0.0000pt;margin-bottom:0.0000pt;margin-left:0.0000pt;text-indent:0.0000pt;text-align:justify;text-justify:inter-ideograph;line-height:21.6000pt;\"><span style=\"font-size:large;\"><img alt=\"\" src=\"http://www.waterrower.com.cn/wl/wl/cm/wlCmDocumentAction.dox?action=downloadFile&id=297ebc2d5fbd3463015fbd9d8d40002b\" style=\"display:block;margin-left:auto;margin-right:auto;\" /></span></p><p style=\"text-indent:21.0000pt;\"><br /></p><div><br /></div><div><br /></div>";
		str=HtmlRegexpUtil.filterHtml(str);	
		System.out.println("str="+str);
	}
}
