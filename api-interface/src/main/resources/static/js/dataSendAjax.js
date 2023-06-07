var token = $("input[name='_csrf']").val();
var header = "X-CSRF-TOKEN";

// login 데이터 ajax 전송
function loginDataSend() {
    var email = $('#email').val();
    var password = $('#password').val();
    $.ajax({
        beforeSend: function (xhr) {
            xhr.setRequestHeader(header, token);
        },
        type: "post",
        url: "/auth/login",
        dataType: "json",
        data: { email: email, password: password },
        success: function (res) {
          loginDataSendSuccess(res);
        },
        error: function (res) {
          loginDataSendFail(res);
        }
    });
}

// login ajax success 이후 처리
function loginDataSendSuccess(res) {
  window.location.href = res.redirectURI;
}

function loginDataSendFail(res) {
  if(res.status == "400") alert("로그인 정보가 일치하지 않습니다");
  else alert("server error");
}
