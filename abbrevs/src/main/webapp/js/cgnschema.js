if (!window.Raphael) {
  document.write('<script type="text/javascript" src="js/raphael-min.js"></script>');
}

window.CgnSchema || (function(){
  var g = {
      win: window,
      doc: window.document
    },
    CS = function() {},
    CSView = function() {
      this.conceptViews = [];
    };
  
  CSView.prototype.addConcept = function(concept) {
    var p = this._paper, frame, group, text;
    p.setStart();
    frame = p.rect(-50, -25, 100, 50, 10).attr("fill", "#cfc");
    text = p.text(0, 0, concept.name);
    
    group = p.setFinish();
    group.data("concept", concept);
    group.translate(100, 100);

//    text.attr("href", "#edit");

    var sx, sy = 0;

    frame.drag(function(dx, dy) {
      group.transform(["t", frame.ox + dx, frame.oy + dy]);
    }, function() {
      frame.animate({"fill-opacity": 0.5}, 200);
      var ts = frame.transform(), tx = 0, ty = 0;
      group.toFront();

      for (i = 0, ii = ts.length; i < ii; ++i) {
        var t = ts[i];
        if (t[0] == "t") {
          tx += t[1];
          ty += t[2];
        }
      }

      frame.ox = tx;
      frame.oy = ty;
    }, function() {
      frame.animate({"fill-opacity": 1}, 200);
    });

    var views = this.conceptViews;
    for (i = 0, ii = views.length; i < ii; ++i) {
      views[i].translate(120, 0);
    }
    views.push(group);
  }

  CS.init = function(paper) {
    paper.clear();
    var view = new CSView();
    view._paper = paper;
    return view;
  }

  g.win.CgnSchema = CS;

})();

$(function() {
  var cont = $("#cgnschema-display");
  var paper = Raphael("cgnschema-display", cont.width(), 500);
  paper.setViewBox(-200, -200, 400, 400);

  var schema = CgnSchema.init(paper);
  schema.addConcept({name: "Понятие 1"});
  schema.addConcept({name: "Понятие 2"});
  schema.addConcept({name: "Понятие 3"});
  schema.addConcept({name: "Понятие 4"});
});