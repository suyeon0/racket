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
        error: function () {
          console.log(error);
        }
    });
}

// login ajax success 이후 처리
function loginDataSendSuccess(res) {
    if (res.result == "SUCCESS") {
        window.location.href = res.redirectURI;
    } else if (res.result == "FAIL") {
        alert("로그인 정보와 일치하는 유저가 없습니다");
        return false;
    } else {
        alert("SERVER ERROR");
        return false;
    }
}
