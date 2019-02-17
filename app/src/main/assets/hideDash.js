javascript: (
    function() {
        document
            .getElementsByClassName('position-relative js-header-wrapper ')[0]
            .style
            .display = 'none';
        document.getElementsByClassName('footer container-lg px-3')[0]
            .style
            .display = 'none';
    }
)()