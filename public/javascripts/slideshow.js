$(document).ready(function() {
    var slides = [];
    var curSlide = 0;
    function renderSlide() {
        $("#wmd-preview").html(converter.makeHtml(slides[curSlide].markdown)); 
        $('pre code').each(function(i, e) {hljs.highlightBlock(e)});
    }
    function nextSlide() {
        curSlide = Math.min(slides.length-1, curSlide+1);
        renderSlide();
    }
    function prevSlide() {
        curSlide = Math.max(0, curSlide-1);
        renderSlide();
    }
    var converter = new Markdown.Converter();
    converter.hooks.chain("postConversion", function (text) {
        return text.replace('<code>','<code class="scala">');
    });
    $.get('/slides/slides',  function(data) {
         slides = data;
         renderSlide();
    });
    $(document).keydown(function(e){
        if (e.keyCode == 37) { 
            prevSlide();
            return false;
        } else if (e.keyCode == 39) {
            nextSlide();
            return false;
        } 
    });
    hljs.initHighlightingOnLoad();
});