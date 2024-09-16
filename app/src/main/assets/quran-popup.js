jQuery(document).ready(function($) {
    $('.popup-trigger').click(function(event) {
        event.preventDefault(); // Prevent default action

        var chapter = $(this).data('chapter');
        var verse = $(this).data('verse');
        if (typeof verse === 'string') {
            verse = verse.split('-')[0]; // Use only the start verse if there's a range
        }
        var name = $(this).data('name');

        var popupHtml = '<div class="quran-popup">' +
            '<button onclick="window.open(\'https://www.tamilquran.in/quran1.php?id=100' + chapter + '#' + verse + '\', \'_blank\')">Tamil Quran</button>' +
            '<button onclick="window.open(\'https://www.tamililquran.com/qurandisp.php?start=' + chapter + '#' + chapter + ':' + verse + '\', \'_blank\')">Tamilil Quran</button>' +
            '<button onclick="window.open(\'https://corpus.quran.com/treebank.jsp?chapter=' + chapter + '&verse=' + verse + '\', \'_blank\')">Corpus Quran (Treebank)</button>' +
            '<button onclick="window.open(\'https://tafsir.app/ayah-morph/' + chapter + '/' + verse + '\', \'_blank\')">Tafsir App (Morphology)</button>' +
            '<button class="close-popup">Close</button>' +
            '</div>';

        // Remove any existing popup
        $('.quran-popup').remove();

        // Append the new popup
        $('body').append(popupHtml);

        // Position the popup near the clicked element
        var offset = $(this).offset();
        $('.quran-popup').css({ top: offset.top + 20, left: offset.left });

        // Add the show class to trigger the animation
        setTimeout(function() {
            $('.quran-popup').addClass('show');
        }, 10);

        // Close the popup when the close button is clicked
        $('.close-popup').click(function() {
            $('.quran-popup').removeClass('show');
            setTimeout(function() {
                $('.quran-popup').remove();
            }, 300); // Wait for the animation to complete
        });
    });

    // Close the popup if clicked outside of it
    $(document).click(function(event) { 
        if(!$(event.target).closest('.popup-trigger, .quran-popup').length) {
            if($('.quran-popup').length) {
                $('.quran-popup').removeClass('show');
                setTimeout(function() {
                    $('.quran-popup').remove();
                }, 300); // Wait for the animation to complete
            }
        }        
    });
});
