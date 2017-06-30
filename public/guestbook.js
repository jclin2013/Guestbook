let successPage =
`<div id='successPageContainer'>
  <div id='successPageCenter'>
    <text>Thanks! Your info was saved.</text>
    <button id="backToHomeButton">Back to home.</button>
  </div>
</div>`

$('#submitButton').click(() => $('body').append(successPage));

$('#backToHomeButton').click(() => $('#successPageContainer').remove());
