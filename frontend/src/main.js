import { navigateTo } from './router.js';

// ---------- Sidebar: recolher/expandir ----------
const sidebar = document.querySelector('.sidebar');
const sidebarToggle = document.getElementById('sidebarToggle');

sidebarToggle.addEventListener('click', () => {
  sidebar.classList.toggle('collapsed');
  document.body.classList.toggle('sidebar-collapsed');
});

// ---------- Sidebar: gavetas dos módulos ----------
const moduleToggles = document.querySelectorAll('.module-toggle');

moduleToggles.forEach((toggle) => {
  toggle.addEventListener('click', () => {
    const module = toggle.closest('.nav-module');
    const wasOpen = module.classList.contains('open');

    document.querySelectorAll('.nav-module').forEach((m) => m.classList.remove('open'));

    if (!wasOpen) {
      module.classList.add('open');
    }
  });
});

// ---------- Navegação entre páginas ----------
const pageLinks = document.querySelectorAll('.page-link');

pageLinks.forEach((link) => {
  link.addEventListener('click', () => {
    const page = link.dataset.page;
    setActiveLink(link);
    navigateTo(page);
  });
});

function setActiveLink(activeLink) {
  pageLinks.forEach((link) => link.classList.remove('active'));
  activeLink.classList.add('active');
}

// ---------- Estado inicial ----------
function openModuleOf(link) {
  const module = link.closest('.nav-module');
  if (module) module.classList.add('open');
}

const initialPage = 'principal/visao-geral';
const initialLink = document.querySelector(`.page-link[data-page="${initialPage}"]`);

if (initialLink) {
  setActiveLink(initialLink);
  openModuleOf(initialLink);
}

navigateTo(initialPage);
