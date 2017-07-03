var datagrid = new DatabaseGrid();

window.onload = function() {
  // key typed in the filter field
  $("#filter").keyup(function() {
      datagrid.editableGrid.filter( $(this).val());

      // To filter on some columns, you can set an array of column index
      // datagrid.editableGrid.filter( $(this).val(), [0,3,5]);
    });

  $("#showaddformbutton").click( function()  {
    showAddForm();
  });
  $("#cancelbutton").click( function() {
    showAddForm();
  });

  $("#addbutton").click(function() {
    datagrid.addRow();
  });

  $('#uncheckPageButton').click(function() {
    $(":checkbox:checked").each(function () { this.click(); });
  });

  let responseFunc = response => {
        if (response === "Email Sent!") {
          $('#message').toggleClass("success")
          $('#message').html("Email Sent");
          setTimeout(() => $('#message').toggleClass("success"), 5000);
          $('#emailSubject, #emailBody').val("");
        } else {
          console.log(response);
          $('#message').toggleClass("fail")
          $('#message').html("Failed To Send");
          setTimeout(() => $('#message').toggleClass("fail"), 5000);
        }
        $('#sendEmailFormOuterContainer').remove();
      }

  let sendEmailForm =
    `<div id="sendEmailFormOuterContainer">
      <div id="sendEmailForm">
        <div id="sendEmailFormInnerContainer">
          <div class="emailInputFields">
            <text>From: java.guestbook.hello@gmail.com</text>
            <text>To:</text>
            <input id="toEmailAddresses"></input>
            <text>Subject:</text>
            <input id="emailSubject"></input>
            <text>Body:</text>
            <textarea id="emailBody"></textarea>
          </div>
          <div id="sendCancelButtonsContainer">
            <button id="submitEmailButton">Send</button>
            <button id="cancelEmailButton">Cancel</button>
          </div>
        </div>
      </div>
    </div>`

  // let spinner = () => {
  //   $("#sendEmailFormInnerContainer").append(`<div></div>`);
  // }

  let triggerSubmitEmail = () => {
      let encodedAddresses = encodeURIComponent($('#toEmailAddresses').val()),
          encodedSubject = encodeURIComponent($('#emailSubject').val()),
          encodedBody = encodeURIComponent($('#emailBody').val());

      let addresses = `?addresses=${encodedAddresses}`,
          subject = `&subject=${encodedSubject}`,
          body = `&body=${encodedBody}`;

      // spinner();

      $.ajax({method: "POST",
              url: "/showAllUsers/sendEmail" + addresses + subject + body
            }).then(response => responseFunc(response),
                    fail => console.log(fail));
  };

  let setupClicksAndForm = response => {
    $('body').append(sendEmailForm);

    $('#toEmailAddresses').val(response);

    $('#cancelEmailButton').click(() => {
      $('#emailSubject, #emailBody').val("");
      $('#sendEmailFormOuterContainer').remove();
    });

    $('#submitEmailButton').click(triggerSubmitEmail);
  }

  $('.openEmailButtons').click(e => {
      let url = '/showAllUsers/getEmailAddresses';
      if (e.currentTarget.id === 'sendEmailToAll') {
        url += '?selectedEmails=false';
      } else {
        url += '?selectedEmails=true';
      }

      $.ajax({method: "POST", url})
      	.then(response => setupClicksAndForm(response),
              fail => console.log(fail));
  });

};
