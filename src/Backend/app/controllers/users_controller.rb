class UsersController < ApplicationController
  before_action :set_user, only: %i[ show update destroy show_balance get_transactions ]

  # GET /users
  def index
    @users = User.all

    render json: @users
  end

  # GET /users/1
  def show
    render json: @user
  end

  def show_balance
    # Soma todos os valores das transações
    total_balance = @user.transactions.sum(:valor)
    total_income = @user.transactions.filter { |t| t.valor >= 0 }.map { |t| t.valor }.sum
    total_outgoing = @user.transactions.filter { |t| t.valor < 0 }.map { |t| t.valor }.sum

    # Retorna o total da soma no formato JSON
    render json: { total_balance: total_balance, total_income: total_income, total_outgoing: total_outgoing }
  end


  # POST /users
  def create
    @user = User.new(user_params)

    if @user.save
      render json: @user, status: :created, location: @user
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  # PATCH/PUT /users/1
  def update
    if @user.update(user_params)
      render json: @user
    else
      render json: @user.errors, status: :unprocessable_entity
    end
  end

  # DELETE /users/1
  def destroy
    @user.destroy!
  end

  def login
    @user = User.find_by(login_params)
    if @user
      render json: @user
    else
      render status: :not_found
    end
  end

  def get_transactions
    if @user
      render json: @user.transactions.includes(:transaction_type).order(data: :desc).as_json(include: :transaction_type)
    else
      render status: :not_found
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_user
      @user = User.find(params[:id])
    end

    # Only allow a list of trusted parameters through.
    def user_params
      params.require(:user).permit(:nome, :sobrenome, :cpf, :endereco, :email, :senha)
    end

    def login_params
      params.permit(:email, :senha)
    end
end
