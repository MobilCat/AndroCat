javascript: (
    function() {

        var css = '';

        var issuesNavBarName = 'reponav-wrapper reponav-small d-lg-none';
        var gitHubNavBarName = 'position-relative js-header-wrapper ';

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

        document.body.style.marginBottom='55px'
    })()