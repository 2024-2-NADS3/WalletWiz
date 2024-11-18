Rails.application.routes.draw do
  get "users/:id/show_balance", controller: "users", action: :show_balance
  post "users/login", controller: "users", action: :login
  get "users/:id/transactions", controller: "users", action: :get_transactions

  resources :transactions
  resources :transaction_types
  resources :users
  # Define your application routes per the DSL in https://guides.rubyonrails.org/routing.html

  # Reveal health status on /up that returns 200 if the app boots with no exceptions, otherwise 500.
  # Can be used by load balancers and uptime monitors to verify that the app is live.
  get "up" => "rails/health#show", as: :rails_health_check



  # Defines the root path route ("/")
  # root "posts#index"
end
