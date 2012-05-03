$(document).ready(function () {
    var converter = new Markdown.Converter();
    var editor = new Markdown.Editor(converter);
    var selectedSlide = '.slide:first';
    converter.hooks.chain("postConversion", function (text) {
        return text.replace('<code>', '<code class="scala">');
    });
    editor.hooks.chain("onPreviewRefresh", function () {
        $('pre code').each(function (i, e) {
            hljs.highlightBlock(e)
        });
    });
    editor.run();

    function refreshSlides(callback) {
        $.get('/slides/slides', function (slides) {
            $(".slide").remove();
            $.each(slides, function (i, slide) {
                slide.renderMD = function () {
                    return converter.makeHtml(this.markdown);
                };
                $('#slides').append(Mustache.render('\
                    <li id="slide{{id}}" data-slide-id="{{id}}" data-position="{{position}}" class="slide"> \
                        <a href="#">\
                            <h4>{{position}}</h4> \
                            <div class="miniSlide"> \
                                {{{renderMD}}} \
                            </div> \
                            <form id="deleteForm{{id}}" action="/slides/{{id}}" method="POST"> \
                                <input class="btn btn-primary btn-small" type="submit" value="Delete"/> \
                            </form> \
                        </a> \
                    </li>', slide));
                $('#slide' + slide.id).click(function () {
                    $('.slide').removeClass("active");
                    $('#slide' + slide.id).addClass("active");
                    $("#wmd-input").val(slide.markdown);
                    $("#slideId").val(slide.id);
                    $("#position").val(slide.position);
                    $("#slideForm").attr("action", "/slides/" + slide.id);
                    editor.refreshPreview();
                    selectedSlide = '#slide' + slide.id;
                });
                $('#deleteForm' + slide.id).submit(function (e) {
                    $.ajax({
                        type: 'DELETE',
                        url: $('#deleteForm' + slide.id).attr('action'),
                        success: function() {
                            $("#slide" + slide.id).remove();
                            updatePositions();
                        }
                    });
                    e.preventDefault();
                    return false;
                });
            });
            $(selectedSlide).click();
            if (typeof callback === 'function') callback();
        });
    }

    function updatePositions() {
        var slidePositions = {};
        var position = 0;
        $.each($("#slides").children(".slide"), function (i, child) {
            var slideId = $(child).data("slide-id");
            if (slideId) {
                slidePositions[slideId] = ++position;
                $(child).data("position", position);
            }
        });
        $.ajax({
            type: 'POST',
            url: '/slides/updatePositions',
            data: JSON.stringify(slidePositions),
            contentType: 'application/json',
            success: refreshSlides
        });
    }

    $("#updateForm").submit(function (e) {
        $.ajax({
            type: 'PUT',
            url: $(this).attr('action'),
            data: JSON.stringify({
                markdown: $("#wmd-input").val(),
                id: $("#slideId").val(),
                position: $("#position").val()
            }),
            contentType: 'application/json',
            success: refreshSlides
        });
        e.preventDefault();
        return false;
    });

    refreshSlides(function () {
        $("#slides").sortable();
        $("#slides").bind("sortupdate", updatePositions);
    });
    hljs.initHighlightingOnLoad();
});