$(document).ready(function() {
     var converter = new Markdown.Converter();
     var editor = new Markdown.Editor(converter);
     converter.hooks.chain("postConversion", function (text) {
          return text.replace('<code>','<code class="scala">');
     });
     editor.hooks.chain("onPreviewRefresh", function () {
         $('pre code').each(function(i, e) {hljs.highlightBlock(e)});
     });
     editor.run();
     $("#slideForm").submit(function(e) {
         $.ajax({type:'PUT',
                 url:$(this).attr('action'), 
                 data:JSON.stringify({
                     markdown:$("#wmd-input").val(),
                     id:$("#slideId").val(), 
                     position:$("#position").val()
                 }),
                 dataType:'json',
                 contentType:'application/json'
         });
         e.preventDefault();
         return false;             
     });
     $.get('/slides/slides',  function(slides) {
        $.each(slides, function(i,slide) {
            slide.renderMD = function() {
                return converter.makeHtml(this.markdown);
            };
            $('#slides').append(Mustache.render('\
            <li id="slide{{id}}" data-slide-id="{{id}}" data-position="{{position}}" class="slide"> \
                <a href="#">\
                    <h4>{{position}}</h4> \
                    <div class="miniSlide"> \
                        {{{renderMD}}} \
                    </div> \
                    <form action="/slides/{{id}}" method="DELETE"> \
                        <input class="btn btn-primary btn-small" type="submit" value="Delete"/> \
                    </form> \
                </a> \
            </li>', slide));
            $('#slide' + slide.id).click(function() {
                 $('.slide').removeClass("active");
                 $('#slide' + slide.id).addClass("active");
                 $("#wmd-input").val(slide.markdown); 
                 $("#slideId").val(slide.id); 
                 $("#position").val(slide.position); 
                 $("#slideForm").attr("action","/slides/" + slide.id);
                 editor.refreshPreview();
            });
        });
        $(".slide:first").click();
        $("#slides").sortable();
        $("#slides").bind( "sortupdate", function(event, ui) {
            var slidePositions = {};
            var position = 0;
            $.each($(this).children(".slide"), function(i, child) {
                var slideId = $(child).data("slide-id");
                if(slideId) {
                    slidePositions[slideId] = ++position;
                    $(child).data("position", position);
                }
            });
            $.ajax({type:'POST',
                url:'/slides/updatePositions', 
                data:JSON.stringify(slidePositions), 
                dataType:'json',
                contentType:'application/json',
            });
           });
       });
       $('#wmd-input').change(function() {
          alert('changing');
       });
       hljs.initHighlightingOnLoad();
});