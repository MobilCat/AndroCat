javascript: (
    function() {

        var css = 'html {-webkit-filter: grayscale(50%) invert(100%);}' +
            'img {-webkit-filter:invert(100%);}';

        var leftSideBarName = 'dashboard-sidebar js-sticky top-0 px-3 px-md-4 px-lg-4 overflow-auto';
        var rightSideBarName = 'col-12 col-md-8 col-lg-6 p-responsive mt-3 border-bottom';
        var newsName = 'news';
        var issuesNavBarName = 'reponav-wrapper reponav-small d-lg-none';
        var gitHubNavBarName = 'position-relative js-header-wrapper ';

        var darkDash = '#24282D';
        var darkDashInverted = '#ded7cc';

        if (document.getElementsByClassName(gitHubNavBarName)[0] != null) {
            document.getElementsByClassName(gitHubNavBarName)[0]
                .style
                .display = 'none';
        }
        if (document.getElementsByClassName(issuesNavBarName)[0] != null) {
            document.getElementsByClassName(issuesNavBarName)[0]
                .style
                .display = 'none';
        }

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
        body.style.backgroundColor = darkDash;

        if (document.getElementsByClassName(leftSideBarName)[0] != null) {
            document.getElementsByClassName(leftSideBarName)[0].style.backgroundColor = darkDashInverted;
        }
        if (document.getElementsByClassName(newsName)[0] != null) {
            document.getElementsByClassName(newsName)[0].style.backgroundColor = darkDashInverted;
        }
        if (document.getElementsByClassName(rightSideBarName)[0] != null) {
            document.getElementsByClassName(rightSideBarName)[0].style.backgroundColor = darkDashInverted;
        }
        document.body.style.marginBottom='36px'
    })()