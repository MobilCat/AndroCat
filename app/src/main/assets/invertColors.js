javascript: (
    function() {
        var css = 'html {-webkit-filter: invert(100%);' +
            '-moz-filter: invert(100%);' +
            '-o-filter: invert(100%);' +
            '-ms-filter: invert(100%); }';
        var white = '#ffffff';
        var darkDash = '#1e2326';
        var darkDashInverted = '#e1dcd9'
        var dash = '#24292f';

        head = document.getElementsByTagName('head')[0];
        body = document.getElementsByTagName('body')[0];
        style = document.createElement('style');

        if (!window.counter) {
            window.counter = 1;
            getInvertedColors()
        } else {
            window.counter++;
            if (window.counter % 2 == 0) {
                getNormalColors()
            } else {
                getInvertedColors()
            }
        };


        function getInvertedColors() {
            style.type = 'text/css';
            if (style.styleSheet) {
                style.styleSheet.cssText = css;
            } else {
                style.appendChild(document.createTextNode(css));
            }

            head.appendChild(style);
            body.appendChild(style);
            body.style.backgroundColor = darkDash;
            document.getElementsByClassName('dashboard-sidebar js-sticky top-0 px-3 px-md-4 px-lg-4 overflow-auto')[0].style.backgroundColor = darkDashInverted;
            document.getElementsByClassName('col-12 col-md-8 col-lg-6 p-responsive mt-3 border-bottom')[0].style.backgroundColor = darkDashInverted;
            document.getElementsByClassName('team-left-column col-12 col-md-3 col-lg-3 pr-5 mt-5 hide-lg hide-md hide-sm border-bottom')[0].style.backgroundColor = darkDashInverted;
        }

        function getNormalColors() {
            var css = 'html {-webkit-filter: invert(0%); -moz-filter:    invert(0%); -o-filter: invert(0%); -ms-filter: invert(0%); }'
            style.type = 'text/css';
            if (style.styleSheet) {
                style.styleSheet.cssText = css;
            } else {
                style.appendChild(document.createTextNode(css));
            }

            head.appendChild(style);
            body.appendChild(style);
            body.style.backgroundColor = white;
            document.getElementsByClassName('dashboard-sidebar js-sticky top-0 px-3 px-md-4 px-lg-4 overflow-auto')[0].style.backgroundColor = white;
            document.getElementsByClassName('col-12 col-md-8 col-lg-6 p-responsive mt-3 border-bottom')[0].style.backgroundColor = white;
            document.getElementsByClassName('team-left-column col-12 col-md-3 col-lg-3 pr-5 mt-5 hide-lg hide-md hide-sm border-bottom')[0].style.backgroundColor = white;
        }
    })()