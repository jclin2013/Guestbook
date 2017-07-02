var observer = new MutationObserver(function(mutations) {
    mutations.forEach(function(mutation) {
        console.log(mutation)
        if (mutation.addedNodes && mutation.addedNodes.length > 0) {
            // element added to DOM
            var hasClass = [].some.call(mutation.addedNodes, function(el) {
                return el.classList.contains('MyClass')
            });
            if (hasClass) {
                // element has class `MyClass`
                console.log('element ".MyClass" added');
            }
        }
    });
});

var config = {
    attributes: true,
    childList: true,
    characterData: true
};

observer.observe(document.body, config);
