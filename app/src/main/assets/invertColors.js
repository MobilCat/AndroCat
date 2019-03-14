javascript: (
    function() {
        // the css we are going to inject
        var css = 'html {-webkit-filter: invert(100%);' +
            '-moz-filter: invert(100%);' +
            '-o-filter: invert(100%);' +
            '-ms-filter: invert(100%); }',

            head = document.getElementsByTagName('head')[0];
        body = document.getElementsByTagName('body')[0];
        style = document.createElement('style');

        if (!window.counter) {
            window.counter = 1;
            body.style.backgroundColor = '#090705';
        } else {
            window.counter++;
            if (window.counter % 2 == 0) {
                var css = 'html {-webkit-filter: invert(0%); -moz-filter:    invert(0%); -o-filter: invert(0%); -ms-filter: invert(0%); }'
                body.style.backgroundColor = '#ffffff';
            } else {
                body.style.backgroundColor = '#090705';
            }
        };

        style.type = 'text/css';
        if (style.styleSheet) {
            style.styleSheet.cssText = css;
        } else {
            style.appendChild(document.createTextNode(css));
        }

        head.appendChild(style);
        body.appendChild(style);
    }
)()