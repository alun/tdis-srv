package com.katlex.dsm.snippet

import xml.NodeSeq
import com.katlex.dsm.abbrevs.TDISSiteAbbrevs

class Abbrevs {

  def table(seq : NodeSeq) = <div class="abbrevs">{
      TDISSiteAbbrevs.abbrs.map { kv =>
        <div class="abbrev" abbr={kv._2}>{kv._1}</div>
      } .toSeq
    }</div><div id="abbrevMeaning"></div><script type="text/javascript"><![CDATA[
      $("#abbrevMeaning").css({
        position: "absolute"
      }).hide();

      $(".abbrev").mousemove(function (e) {
        // console.log($(this).attr("abbr"), e.pageX, e.pageY);
        $("#abbrevMeaning").text($(this).attr("abbr")).css({
          left: e.pageX + 10 + "px",
          top: e.pageY + 10 + "px"
        }).show();
      });
      $(".abbrev").mouseout(function () {
        $("#abbrevMeaning").hide();
      });
    ]]></script>
}