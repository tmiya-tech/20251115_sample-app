import http from 'k6/http';
import { sleep } from 'k6';
import { check, fail } from 'k6';

// 環境変数からホスト・ポート・VUSを取得
const HOST = __ENV.HOST || 'localhost';
const PORT = __ENV.PORT || '8080';
const BASE_URL = `http://${HOST}:${PORT}`;

// k6オプション設定
export const options = {
  scenarios: {
    warmup: {
      executor: 'ramping-vus',
      startVUs: 0,
      stages: [
        // WarmUp: 10秒でVUSまでランプアップ
        { duration: '10s', target: parseInt(__ENV.K6_VUS) || 10 }
      ],
      tags: { phase: 'warmup' }
    },
    load_test: {
      executor: 'constant-vus',
      vus: parseInt(__ENV.K6_VUS) || 10,
      duration: '30s',
      startTime: '10s',
      tags: { phase: 'load' }
    },
    cooldown: {
      executor: 'ramping-vus',
      startVUs: parseInt(__ENV.K6_VUS) || 10,
      startTime: '40s',
      stages: [
        // クールダウン: 5秒でVUS=0
        { duration: '5s', target: 0 }
      ],
      tags: { phase: 'cooldown' }
    }
  },
  thresholds: {
    'http_req_duration{phase:load}': ['p(95)<1000'], // 計測期間のみ: 95%のリクエストが1秒以下
    'http_req_failed{phase:load}': ['rate<0.05'], // 計測期間のみ: エラー率5%以下
  },
};

// ランダムなTodoデータ生成
function generateRandomTodo() {
  const titles = [
    '会議の準備', '資料作成', 'コードレビュー', 'テスト実行', 
    'バグ修正', '機能開発', 'ドキュメント更新', 'デプロイ作業'
  ];
  const descriptions = [
    '詳細な作業内容', '重要なタスクです', '優先度高', 
    'レビューが必要', '明日までに完了', '緊急対応'
  ];
  
  return {
    title: titles[Math.floor(Math.random() * titles.length)] + `_${Math.floor(Math.random() * 10000)}`,
    description: descriptions[Math.floor(Math.random() * descriptions.length)],
    completed: Math.random() < 0.3 // 30%の確率でtrue
  };
}

// メインテスト関数
export default function () {
  const todoData = generateRandomTodo();
  
  const payload = JSON.stringify(todoData);
  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  // Todo新規作成APIへのPOSTリクエスト
  const response = http.post(`${BASE_URL}/api/todos`, payload, params);
  
  // レスポンス確認
  const isSuccess = check(response, {
    'status is 200 or 201': (r) => r.status === 200 || r.status === 201,
    'has id in response': (r) => {
      try {
        const body = JSON.parse(r.body);
        return body.id !== undefined;
      } catch (e) {
        return false;
      }
    },
  });

  if (!isSuccess) {
    console.error(`Request failed: ${response.status} ${response.body}`);
  }

  // 短いスリープでリクエスト間隔を制御
  sleep(0.1);
}

// setup関数: テスト開始時に1回実行
export function setup() {
  console.log(`=== k6 Performance Test Setup ===`);
  console.log(`Target: ${BASE_URL}/api/todos`);
  console.log(`VUs: ${__ENV.K6_VUS || 10}`);
  console.log(`Test stages: 10s warmup -> 30s load -> 5s cooldown`);
  console.log(`================================`);
  
  // ヘルスチェック
  const healthCheck = http.get(`${BASE_URL}/api/todos`);
  if (healthCheck.status !== 200) {
    fail(`Health check failed: ${healthCheck.status}`);
  }
}

// teardown関数: テスト終了時に1回実行
export function teardown(data) {
  console.log(`=== Test Completed ===`);
}