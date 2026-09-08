// Módulo: principal — Página: visão geral

export function init(container) {
  updateTimestamp(container);
  animateGoalBars(container);
}

function updateTimestamp(container) {
  const el = container.querySelector('[data-updated-at]');
  if (!el) return;

  const now = new Date();
  el.textContent = now.toLocaleString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
}

function animateGoalBars(container) {
  const bars = container.querySelectorAll('.goal-bar > span[data-value]');

  bars.forEach((bar) => {
    const target = bar.dataset.value;
    bar.style.width = '0%';
    requestAnimationFrame(() => {
      bar.style.transition = 'width 0.8s ease';
      bar.style.width = `${target}%`;
    });
  });
}
