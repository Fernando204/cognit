// Página externa: login / registro / recuperar senha (mesma página, cards diferentes)

const forms = {
  login: document.getElementById('loginForm'),
  register: document.getElementById('registerForm'),
  recover: document.getElementById('recoverForm'),
};

function showForm(name) {
  Object.entries(forms).forEach(([key, form]) => {
    if (!form) return;
    form.classList.toggle('is-active', key === name);
  });
}

document.querySelectorAll('[data-target]').forEach((btn) => {
  btn.addEventListener('click', () => showForm(btn.dataset.target));
});

// ---------- Envio dos formulários (placeholder de integração) ----------

forms.login?.addEventListener('submit', (event) => {
  event.preventDefault();
  // TODO: autenticar com o backend
  window.location.href = 'estabelecimentos.html';
});

forms.register?.addEventListener('submit', (event) => {
  event.preventDefault();
  // TODO: criar conta no backend
  showForm('login');
});

forms.recover?.addEventListener('submit', (event) => {
  event.preventDefault();
  // TODO: solicitar link de recuperação ao backend
  showForm('login');
});
