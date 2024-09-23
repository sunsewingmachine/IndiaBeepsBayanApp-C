jQuery(document).ready(function($) {
    var currentAudio;

    $('.popup-trigger').click(function(event) {
        event.preventDefault();

        var chapter = $(this).data('chapter');
        var verse = $(this).data('verse');
        if (typeof verse === 'string') {
            verse = verse.split('-')[0];
        }

        var chapterNumber = chapter.toString().padStart(3, '0');
        var verseNumber = verse.toString().padStart(3, '0');

        const alafasyAudioUrl = `https://everyayah.com/data/Alafasy_128kbps/${chapterNumber}${verseNumber}.mp3`;
        const sudaisAudioUrl = `https://everyayah.com/data/Abdurrahmaan_As-Sudais_64kbps/${chapterNumber}${verseNumber}.mp3`;
        const shuraymAudioUrl = `https://everyayah.com/data/Saood_ash-Shuraym_64kbps/${chapterNumber}${verseNumber}.mp3`;

        var popupHtml = '<div class="quran-popup">' +
            '<div style="display: flex; justify-content: space-between; margin-top: 10px;">' +
                '<button style="margin: 0px;" id="play-alafasy">Alafasy</button>' +
                '<button style="margin: 0px 3px;" id="play-sudais">Sudais</button>' +
                '<button style="margin: 0px;" id="play-shuraym">Shuraym</button>' +
            '</div>' +
            '<button onclick="window.open(\'https://www.tamilquran.in/quran1.php?id=100' + chapter + '#' + verse + '\', \'_blank\')">Tamil Quran</button>' +
            '<button style="background-color: #007b35;" onclick="window.open(\'https://www.tamililquran.com/qurandisp.php?start=' + chapter + '#' + chapter + ':' + verse + '\', \'_blank\')">Tamilil Quran</button>' +
            '<button onclick="window.open(\'https://corpus.quran.com/treebank.jsp?chapter=' + chapter + '&verse=' + verse + '\', \'_blank\')">Corpus Quran (Treebank)</button>' +
            '<button onclick="window.open(\'https://tafsir.app/ayah-morph/' + chapter + '/' + verse + '\', \'_blank\')">Tafsir App (Morphology)</button>' +
            '<button class="close-popup">Stop/Close</button>' +
            '</div>';

        $('.quran-popup').remove();
        $('body').append(popupHtml);

        var offset = $(this).offset();
        $('.quran-popup').css({ top: 0, left: 0});

        setTimeout(function() {
            $('.quran-popup').addClass('show');
        }, 10);

        $('#play-alafasy').click(function() {
            playAudio(alafasyAudioUrl);
        });

        $('#play-sudais').click(function() {
            playAudio(sudaisAudioUrl);
        });

        $('#play-shuraym').click(function() {
            playAudio(shuraymAudioUrl);
        });

        $('.close-popup').click(function() {
            stopAudio();
            $('.quran-popup').removeClass('show');
            setTimeout(function() {
                $('.quran-popup').remove();
            }, 300);
        });
    });

    $(document).click(function(event) {
        if(!$(event.target).closest('.popup-trigger, .quran-popup').length) {
            if($('.quran-popup').length) {
                $('.quran-popup').removeClass('show');
                setTimeout(function() {
                    $('.quran-popup').remove();
                }, 300);
            }
        }
    });

    function playAudio(url) {
        if (currentAudio) {
            currentAudio.pause();
            currentAudio.currentTime = 0;
        }
        currentAudio = new Audio(url);
        currentAudio.play();
    }

    function stopAudio() {
        if (currentAudio) {
            currentAudio.pause();
            currentAudio.currentTime = 0;
            currentAudio = null;
        }
    }
});
