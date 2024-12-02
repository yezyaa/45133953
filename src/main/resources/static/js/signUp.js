document.addEventListener('DOMContentLoaded', function () {
    const signUpForm = document.getElementById('signUpForm');

    signUpForm.addEventListener('submit', function (event) {
        event.preventDefault(); // 폼 기본 동작 방지

        const email = document.getElementById('email').value;
        const name = document.getElementById('name').value;
        const password = document.getElementById('password').value;
        const passwordCheck = document.getElementById('passwordCheck').value;

        if (password !== passwordCheck) {
            alert('비밀번호가 일치하지 않습니다.');
            return;
        }

        // API 요청
        fetch('/api/auth/sign-up', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, name, password, passwordCheck }),
        })
            .then(response => {
                if (response.ok) {
                    alert('회원가입이 성공적으로 완료되었습니다!');
                    window.location.href = '/signIn.html'; // 로그인 페이지로 이동
                } else {
                    return response.json().then(err => {
                        throw new Error(err.message || '회원가입 실패');
                    });
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                alert(`회원가입에 실패했습니다: ${error.message}`);
            });
    });
});
