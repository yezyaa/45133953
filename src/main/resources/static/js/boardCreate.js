document.addEventListener('DOMContentLoaded', function () {
    const createPostForm = document.getElementById('createPostForm');
    const cancelButton = document.getElementById('cancelButton');

    createPostForm.addEventListener('submit', function (event) {
        event.preventDefault(); // 기본 폼 제출 동작 방지

        const formData = new FormData(createPostForm);

        fetch('/api/board', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // JWT 토큰 추가
            },
            body: formData
        })
            .then(response => {
                if (response.ok) {
                    alert('게시글이 작성되었습니다.');
                    window.location.href = '/boardList.html'; // 게시판 목록 페이지로 이동
                } else {
                    return response.json().then(error => {
                        throw new Error(error.message || '게시글 작성에 실패했습니다.');
                    });
                }
            })
            .catch(error => {
                console.error('게시글 작성 에러:', error.message);
                alert(`게시글 작성에 실패했습니다: ${error.message}`);
            });
    });

    // 취소 버튼 클릭 시 게시판 목록으로 이동
    cancelButton.addEventListener('click', function () {
        window.location.href = '/boardList.html';
    });

    const fileInputsContainer = document.getElementById('fileInputs');
    const maxFileInputs = 5; // 최대 파일 입력 필드 개수 제한

    // 파일 입력 필드 동적 추가
    fileInputsContainer.addEventListener('change', function (event) {
        const target = event.target;

        // 파일이 선택된 경우
        if (target.classList.contains('file-input') && target.files.length > 0) {
            const currentFileInputs = fileInputsContainer.querySelectorAll('.file-input');

            // 최대 개수 제한 확인
            if (currentFileInputs.length >= maxFileInputs) {
                alert(`첨부파일은 최대 ${maxFileInputs}개까지 추가할 수 있습니다.`);
                return;
            }

            // 새로운 파일 입력 필드 추가
            const newInput = document.createElement('input');
            newInput.type = 'file';
            newInput.name = 'attachments';
            newInput.className = 'file-input';
            fileInputsContainer.appendChild(newInput);
        }
    });
});
