// Página externa: seleção de estabelecimento (entre o login e o sistema)

document.querySelectorAll('.est-card[data-est]').forEach((card) => {
  card.addEventListener('click', () => {
    // TODO: persistir o estabelecimento selecionado (ex.: sessionStorage / backend)
    sessionStorage.setItem('estabelecimentoAtivo', card.dataset.est);
    window.location.href = 'main.html';
  });
});

document.getElementById('addEstBtn')?.addEventListener('click', () => {
  // TODO: abrir fluxo de cadastro de um novo estabelecimento
});

document.getElementById('logoutBtn')?.addEventListener('click', () => {
  window.location.href = 'loginPage.html';
});
