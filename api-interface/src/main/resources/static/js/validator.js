//공백 확인 함수
function checkExistData(value, dataName) {
    if (value == "") {
        alert(dataName + " 입력해주세요!");
        return false;
    }
    return true;
}

// 유효성 검사 - 이메일
function checkEmail(email) {
    if (!checkExistData(email.value, "이메일을")) {
        email.focus();
        return false;
    }
    return true;
}

// 유효성 검사 - 패스워드
function checkPassword(password) {
    if (!checkExistData(password.value, "패스워드를")) {
        pwd.focus();
        return false;
    }
    return true;
}

// 로그인 입력값 체크
function loginChk() {
    var email = document.getElementById("email");
    var password = document.getElementById("password");
    if (checkEmail(email) && checkPassword(password)) {
        return true;
    }
}