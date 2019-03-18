javascript: (
    function() {
        document.getElementsByClassName('position-relative js-header-wrapper ')[0]
                .style
                .display = 'none';

        var css = 'html {-webkit-filter: invert(0%);}' +
            'img {-webkit-filter:invert(0%);}';

        var leftSideBarName = 'dashboard-sidebar js-sticky top-0 px-3 px-md-4 px-lg-4 overflow-auto';
        var rightSideBarName = 'col-12 col-md-8 col-lg-6 p-responsive mt-3 border-bottom';
        var newsName = 'team-left-column col-12 col-md-3 col-lg-3 pr-5 mt-5 hide-lg hide-md hide-sm border-bottom';

        var white = '#ffffff';
        var lightBlue = '#f6f8fa';

        head = document.getElementsByTagName('head')[0];
        body = document.getElementsByTagName('body')[0];
        style = document.createElement('style');



        style.type = 'text/css';
        if (style.styleSheet) {
            style.styleSheet.cssText = css;
        } else {
            style.appendChild(document.createTextNode(css));
        }

        head.appendChild(style);
        body.appendChild(style);
        body.style.backgroundColor = white;

        document.getElementsByClassName(leftSideBarName)[0].style.backgroundColor = white;
        document.getElementsByClassName(rightSideBarName)[0].style.backgroundColor = lightBlue;
        document.getElementsByClassName(newsName)[0].style.backgroundColor = lightBlue;
    })()