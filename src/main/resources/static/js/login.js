function login() {
  const username = document.getElementById("data-username").value;
  const password = document.getElementById("data-password").value;

  if (username === "" || password === "") {
    return console.error("[Error] username or password is empty");
  }

  get({
    url: "/user/login",
    urlParams: {
      username,
      password,
    },
    onSuccess: (data) => {
      console.log(data);
    },
    onError: (error) => {
      const response = JSON.parse(error.response);
      if (response.status === 401) {
        const errorMsgDom = document.getElementById("login-fail-msg");
        errorMsgDom.style.display = "block";
        errorMsgDom.innerText = '등록되어 있지 않은 유저입니다.'; /* response.message alternative */
      }
      console.error(error);
    },
  });
}